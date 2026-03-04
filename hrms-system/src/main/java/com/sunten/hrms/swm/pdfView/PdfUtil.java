package com.sunten.hrms.swm.pdfView;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.lowagie.text.Element;

import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.domain.SwmBonusPayment;
import com.sunten.hrms.swm.domain.SwmWageSummaryFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfUtil {
    private String empName;
    private Font fontDepartment;

    public PdfUtil(String empName) throws Exception {
        this.empName = empName;
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
//        Font fontChinese = new Font(bfChinese, 25, Font.NORMAL);
//        fontChinese.setColor(BaseColor.BLACK);
//        fontChinese.setSize(25);
//
        this.fontDepartment = new Font(bfChinese, 9, Font.NORMAL);
        fontDepartment.setColor(BaseColor.BLACK);
        fontDepartment.setSize(9);
    }

    public void exportBonusPaymentPdf(HttpServletResponse response, List<SwmBonusPayment> swmBonusPayments, SwmBonus swmBonus) throws Exception {
        //告诉浏览器用什么软件可以打开此文件

        Document document = getDocument(response, true);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        BonusHeaderHandle headerHandle = new BonusHeaderHandle(empName, "顺特电气设备有限公司奖金发放表", swmBonus); // 文档名
        writer.setPageEvent(headerHandle);
        // 打开文档
        document.open();
        //设置文档标题
        document.addTitle("奖金发放表");
        //设置文档作者
        document.addAuthor("sunten");
        // 自动创建时间
        document.addCreationDate();
        //设置关键字
        document.addKeywords("iText");
        // 记录当前部门、 科室
        String department = "";
        String administrativeOffice = "";
        // 专门写入部门抬头
        Paragraph departmentParagraph;
        // 专门写入科室抬头
        Paragraph administrativeOfficeParagraph;

        // 存储科室小计
        SwmBonusPayment administrativeOfficeBonusPayment = new SwmBonusPayment();
        // 存储部门小计
        SwmBonusPayment departmentBonusPayment = new SwmBonusPayment();
        // 存储合计
        SwmBonusPayment sumBonusPayment = new SwmBonusPayment();
        // 行高
        float lineHeight = (float)11;
        // 通用table
        PdfPTable table = new PdfPTable(7);
        // 序号
        Integer count = 1;
        for (SwmBonusPayment swm : swmBonusPayments
             ) {
            if (!swm.getDepartment().equals(department)) { // 部门切换
                if (!department.equals("") ) {
                    // 提交表
                    document.add(table);
                    // 建立科室小计
                    table = new PdfPTable(7);
                    table.setSplitLate(false);
                    table.setSplitRows(true);
                    bonusPaymentAddCell(table, administrativeOfficeBonusPayment, false, false);
                    document.add(setSwmBonusPaymentStyle(table));
                    // 重置科室
                    administrativeOfficeBonusPayment = new SwmBonusPayment();

                    // 建立部门小计
                    table = new PdfPTable(7);
                    table.setSplitLate(false);
                    table.setSplitRows(true);
                    bonusPaymentAddCell(table, departmentBonusPayment, true, true);
                    document.add(setSwmBonusPaymentStyle(table));
                    // 重置部门
                    departmentBonusPayment = new SwmBonusPayment();

                }
                // 加个间隔
                document.add(Chunk.NEWLINE); // 新建一行

                // 建立部门抬头
                departmentParagraph =
                        new Paragraph("部门  " + swm.getDepartment(), fontDepartment);
                document.add(departmentParagraph);
                // 建立科室抬头
                administrativeOfficeParagraph = new Paragraph("子部门  " + (null == swm.getAdministrativeOffice() ? "" : swm.getAdministrativeOffice()), fontDepartment);
                document.add(administrativeOfficeParagraph);
                // 加个间隔
//                  document.add(Chunk.NEWLINE); // 新建一行
                // 建立抬头
                generateBonusPaymentHeader(document, lineHeight);
                // 建立table
                table = new PdfPTable(7);
                department = swm.getDepartment();
                administrativeOffice = swm.getAdministrativeOffice();

            }
            else if (swm.getAdministrativeOffice() != null && !swm.getAdministrativeOffice().equals(administrativeOffice) && swm.getDepartment().equals(department)) { // 发生科室切换
                // 建立科室小计
                table = new PdfPTable(7);
                bonusPaymentAddCell(table, administrativeOfficeBonusPayment, false, true);
                document.add(setSwmBonusPaymentStyle(table));
                // 重置科室
                administrativeOfficeBonusPayment = new SwmBonusPayment();

                // 加个间隔
                document.add(Chunk.NEWLINE); // 新建一行

                // 建立科室抬头
                administrativeOfficeParagraph  =
                        new Paragraph("子部门  " + (swm.getAdministrativeOffice() == null ? "" : swm.getAdministrativeOffice()), fontDepartment);
                document.add(administrativeOfficeParagraph);
                // 加个间隔
//                  document.add(Chunk.NEWLINE); // 新建一行
                // 建立抬头
                generateBonusPaymentHeader(document, lineHeight);

                // 建立table
                table = new PdfPTable(7);
                administrativeOffice = swm.getAdministrativeOffice();
            }
            doSumBonusPayment(departmentBonusPayment,swm);
            // 科室累计
            doSumBonusPayment(administrativeOfficeBonusPayment, swm);
            // 合计
            doSumBonusPayment(sumBonusPayment, swm);
            // 增加行
            bonusPaymentTableAddCell(table, swm, count);
            document.add(setSwmBonusPaymentStyle(table));
            count ++;
            table = new PdfPTable(7);
        }
        // 最后再做一次累计
        // 提交表
        document.add(setSwmBonusPaymentStyle(table));
        // 建立科室小计
        table = new PdfPTable(7);
        bonusPaymentAddCell(table, administrativeOfficeBonusPayment, false, false);
        document.add(setSwmBonusPaymentStyle(table));

        // 建立部门小计
        table = new PdfPTable(7);
        bonusPaymentAddCell(table, departmentBonusPayment, true, false);
        document.add(setSwmBonusPaymentStyle(table));

        // 建立合计
        table = new PdfPTable(7);
        sumBonusPaymentAddCell(table, sumBonusPayment);
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
                false);
        Font fontEnd = new Font(bfChinese, 10, Font.BOLD);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日");
        ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("制表人: " + empName, fontEnd), document.getPageSize().getRight() / 4 - 70 ,document.bottom() - 15, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("审核:" , fontEnd), document.getPageSize().getRight() / 2 - 120 ,document.bottom() - 15, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("财务总监:" , fontEnd), document.getPageSize().getRight() / 2 - 10 ,document.bottom() -15, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("批准:" , fontEnd), document.getPageSize().getRight() / 2 + 90 ,document.bottom() - 15, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase(LocalDate.now().format(fmt), fontEnd), document.getPageSize().getRight()  - 65 ,document.bottom() - 15, 0);
        document.add(setSwmBonusPaymentStyle(table));
        // 建立
        document.close();
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/3/10 9:40
     *  @Description: rotate true 为正向， false 为横向
     *  @params:
     */
    private Document getDocument(HttpServletResponse response, Boolean rotate) throws DocumentException, IOException {
        response.setHeader("content-Type", "application/pdf");
        //下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=XXX.pdf");
        Rectangle tRectangle = new Rectangle(PageSize.A4); // 页面大小
        Document document;
        if (rotate) {
            document = new Document(tRectangle); // 正向
        } else {
            document = new Document(tRectangle.rotate()); // 横向
        }
        document.setMargins(10, 10, 60, 30);// 页边空白
        document.setMarginMirroring(false);
        return document;
    }


    /**
     *  @author：liangjw
     *  @Date: 2021/1/10 9:35
     *  @Description: 工资汇总个人导出
     *  @params:
     */
      public  void exportPdf( HttpServletResponse response,List<SwmWageSummaryFile> swmWageSummaryFiles,String incomePeriod) throws Exception {
          //告诉浏览器用什么软件可以打开此文件
          Document document = getDocument(response, false);
          PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
          HeaderHandle headerHandle = new HeaderHandle(empName, "  顺特电气设备工资发放表 (" + incomePeriod + ")", null);
          writer.setPageEvent(headerHandle);
          // 打开文档
          document.open();
          //设置文档标题
          document.addTitle("个人工资报表");
          //设置文档作者
          document.addAuthor("sunten");
          // 自动创建时间
          document.addCreationDate();
          //设置关键字
          document.addKeywords("iText");

          // 记录当前部门、 科室
          String department = "";
          String administrativeOffice = "";
          // 专门写入部门抬头
          Paragraph departmentParagraph;
          // 专门写入科室抬头
          Paragraph administrativeOfficeParagraph;

          // 存储科室小计
          SwmWageSummaryFile administrativeOfficeSwmWage = new SwmWageSummaryFile();
          // 存储部门小计
          SwmWageSummaryFile departmentSwmWage = new SwmWageSummaryFile();
          // 存储总计
          SwmWageSummaryFile allSumWage = new SwmWageSummaryFile();
          // 行高
          float lineHeight1 = (float)14;
          float lineHeight2 = (float)14;
          // 通用table
          PdfPTable table = new PdfPTable(21);
          table.setSplitLate(false);
          table.setSplitRows(true);
          // 序号
          Integer count = 1;
          // 标题
//          Paragraph paragraph =
//                  new Paragraph("顺特电气设备工资发放表", fontChinese);
//          // 1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
//          paragraph.setAlignment(1);
//          document.add(paragraph);

          for (SwmWageSummaryFile swm : swmWageSummaryFiles
               ) {
              if (!swm.getDepartment().equals(department)) { // 部门切换
                  if (!department.equals("") ) {
                      // 提交表
                      document.add(table);
                      // 建立科室小计
                      table = new PdfPTable(21);
                      administrativeOfficeAddCell(table, administrativeOfficeSwmWage, true);
                      document.add(setTableStyle(table));
                      // 重置科室
                      administrativeOfficeSwmWage = new SwmWageSummaryFile();

                      // 建立部门小计
                      table = new PdfPTable(21);
                      departmentAddCell(table, departmentSwmWage, false);
                      document.add(setTableStyle(table));
                      // 重置部门
                      departmentSwmWage = new SwmWageSummaryFile();

                  }
                  // 加个间隔
                  document.add(Chunk.NEWLINE); // 新建一行

                  // 建立部门抬头
                  departmentParagraph =
                          new Paragraph("部门  " + swm.getDepartment(), fontDepartment);
                  document.add(departmentParagraph);
                  // 建立科室抬头
                  administrativeOfficeParagraph = new Paragraph("子部门  " + (null == swm.getAdministrativeOffice() ? "" : swm.getAdministrativeOffice()), fontDepartment);
                  document.add(administrativeOfficeParagraph);
                  // 加个间隔
//                  document.add(Chunk.NEWLINE); // 新建一行
                  // 建立抬头
                  generateHeader(document, lineHeight1, lineHeight2);
                  // 建立table
                  table = new PdfPTable(21);
                  department = swm.getDepartment();
                  administrativeOffice = swm.getAdministrativeOffice();

              }
              else if (swm.getAdministrativeOffice() != null && !swm.getAdministrativeOffice().equals(administrativeOffice) && swm.getDepartment().equals(department)) { // 发生科室切换
                  // 建立科室小计
                  table = new PdfPTable(21);
                  administrativeOfficeAddCell(table, administrativeOfficeSwmWage, false);
                  document.add(setTableStyle(table));
                  // 重置科室
                  administrativeOfficeSwmWage = new SwmWageSummaryFile();

                  // 加个间隔
                  document.add(Chunk.NEWLINE); // 新建一行

                  // 建立科室抬头
                  administrativeOfficeParagraph  =
                          new Paragraph("子部门  " + (swm.getAdministrativeOffice() == null ? "" : swm.getAdministrativeOffice()), fontDepartment);
                  document.add(administrativeOfficeParagraph);
                  // 加个间隔
//                  document.add(Chunk.NEWLINE); // 新建一行
                  // 建立抬头
                  generateHeader(document, lineHeight1, lineHeight2);

                  // 建立table
                  table = new PdfPTable(21);
                  administrativeOffice = swm.getAdministrativeOffice();
              }
              // 部门累计
              doSumWage(departmentSwmWage, swm);
              // 科室累计
              doSumWage(administrativeOfficeSwmWage, swm);
              // 总计
              doSumWage(allSumWage, swm);
              // 增加行
              tableAddCell(table, swm, count);
              document.add(setTableStyle(table));
              count ++;
              table = new PdfPTable(21);
          }
          // 提交表
          document.add(setTableStyle(table));
          // 建立科室小计
          table = new PdfPTable(21);
          administrativeOfficeAddCell(table, administrativeOfficeSwmWage, true);
          document.add(setTableStyle(table));

          // 建立部门小计
          table = new PdfPTable(21);
          departmentAddCell(table, departmentSwmWage, false);
          document.add(setTableStyle(table));

          // 建立总计
          table = new PdfPTable(21);
          departmentAddCell(table, allSumWage, true);
          document.add(setTableStyle(table));
          BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
                  false);
          Font fontEnd = new Font(bfChinese, 10, Font.BOLD);
          DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日");
          ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("制表人: " + empName, fontEnd), document.getPageSize().getRight() / 4 - 140, document.bottom() - 20, 0);
          ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("审核:", fontEnd), document.getPageSize().getRight() / 2 - 180, document.bottom() - 20, 0);
          ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("财务总监:", fontEnd), document.getPageSize().getRight() / 2 - 20, document.bottom() - 20, 0);
          ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase("总经理:", fontEnd), document.getPageSize().getRight() / 2 + 150, document.bottom() - 20, 0);
          ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_CENTER, new Phrase(LocalDate.now().format(fmt), fontEnd), document.getPageSize().getRight() - 90, document.bottom() - 20, 0);
          document.close();





      }

    private void tableAddCell(PdfPTable table, SwmWageSummaryFile swmWageSummaryFile, Integer count) throws Exception {
          // 序号
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(count.toString(), getPdfChineseFont())), "lr"));
        // 工号
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getWorkCard(), getPdfChineseFont())), "right"));
        // 姓名
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getName(), getPdfChineseFont())), "right"));
        setWageSummaryCell(table, swmWageSummaryFile, false);
    }

    private void setWageSummaryCell(PdfPTable table, SwmWageSummaryFile swmWageSummaryFile, Boolean bottomFlag) throws Exception {
        // 岗位技能
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getPostSkillSalary() != null ? swmWageSummaryFile.getPostSkillSalary().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 加班工资
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getOvertimePay() != null ? swmWageSummaryFile.getOvertimePay().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 绩效工资
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getAllocatePerformancePay() != null ? swmWageSummaryFile.getAllocatePerformancePay().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 一孩
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getOneChildSubsidy() != null ? swmWageSummaryFile.getOneChildSubsidy().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 安全奖
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getSafetyAccumulationAward() != null ? swmWageSummaryFile.getSafetyAccumulationAward().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 补贴扣除
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getAllowanceDeduction() != null ? swmWageSummaryFile.getAllowanceDeduction().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 工龄
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getSeniorityAllowance() != null ? swmWageSummaryFile.getSeniorityAllowance().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 高温
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getHighTemperatureSubsidy() != null ? swmWageSummaryFile.getHighTemperatureSubsidy().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 交通
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getTransportationAllowance() != null ? swmWageSummaryFile.getTransportationAllowance().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 岗位
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getPostSubsidy() != null ? swmWageSummaryFile.getPostSubsidy().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 应发
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getWagesPayable() != null ? swmWageSummaryFile.getWagesPayable().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 医药
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getDeductMedicalCosts() != null ? swmWageSummaryFile.getDeductMedicalCosts().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 水电房
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getDeductHydropowerHouse() != null ? swmWageSummaryFile.getDeductHydropowerHouse().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 所得税
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getDeductIncomeTax() != null ? swmWageSummaryFile.getDeductIncomeTax().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 保险
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getPersonalDeductibles() != null ? swmWageSummaryFile.getPersonalDeductibles().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 公积金
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getPersonalDeductAccumulationFund() != null ? swmWageSummaryFile.getPersonalDeductAccumulationFund().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00" , getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 其它
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getDeductOther() != null ? swmWageSummaryFile.getDeductOther().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
        // 实发工资
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmWageSummaryFile.getNetPayment() != null ? swmWageSummaryFile.getNetPayment().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), bottomFlag ? "rb" : "right"));
    }

    private void administrativeOfficeAddCell(PdfPTable table, SwmWageSummaryFile swmWageSummaryFile, Boolean beforeDeptFlag) throws Exception { // 科室小计
        table.addCell(setCellStyle(new PdfPCell(new Paragraph("小计", getPdfChineseFont())), beforeDeptFlag ? "lr" : "lrb"));
        table.addCell(setCellStyle(new PdfPCell(new Paragraph("")), beforeDeptFlag ? "right" : "rb"));
        table.addCell(setCellStyle(new PdfPCell(new Paragraph("")), beforeDeptFlag ? "right" : "rb"));
        setWageSummaryCell(table, swmWageSummaryFile, !beforeDeptFlag);
    }

    private void departmentAddCell(PdfPTable table, SwmWageSummaryFile swmWageSummaryFile,Boolean isAllSum) throws Exception { // 部门小计
        PdfPCell pdfPCell;
        pdfPCell = new PdfPCell(new Paragraph(isAllSum ? "总计" : "部门小计", getPdfChineseFont()));
        pdfPCell.setColspan(3);
        table.addCell(setCellStyle(pdfPCell, "lrb"));
        setWageSummaryCell(table, swmWageSummaryFile, true);
    }




    private void generateHeader(Document document, float lineHeight1, float lineHeight2) throws Exception {

        PdfPCell pdfPCell;
        PdfPTable title2 = new PdfPTable(10);
        title2.setSpacingBefore(5f); // 留间隔
        pdfPCell = new PdfPCell(new Paragraph("序号", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight1);
        setCellStyle(pdfPCell, "lr");
        title2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("工牌", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight1);
        setCellStyle(pdfPCell, "right");
        title2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("姓名", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight1);
        setCellStyle(pdfPCell, "right");
        title2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("岗位技能", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight1);
        setCellStyle(pdfPCell, "right");
        title2.addCell(pdfPCell);

        pdfPCell= new PdfPCell(new Paragraph("加班工资", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight1);
        setCellStyle(pdfPCell, "right");
        title2.addCell(pdfPCell);

        pdfPCell= new PdfPCell(new Paragraph("绩效工资", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight1);
        setCellStyle(pdfPCell, "right");
        title2.addCell(pdfPCell);


        PdfPTable mergeTable1 = new PdfPTable(7);
        pdfPCell = new PdfPCell(new Paragraph("补贴", getPdfChineseFont()));
        pdfPCell.setColspan(7);
        setCellStyle(pdfPCell,"");
        pdfPCell.setFixedHeight(lineHeight1);
        mergeTable1.addCell(pdfPCell);


        pdfPCell = new PdfPCell(new Paragraph("一孩", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable1.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("安全奖", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable1.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("补贴扣除", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable1.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("工龄", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable1.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("高温", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable1.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("交通", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable1.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("岗位", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell, "");
        mergeTable1.addCell(pdfPCell);

        pdfPCell = new PdfPCell(mergeTable1);
        title2.addCell(pdfPCell);


        pdfPCell = new PdfPCell(new Paragraph("应发工资", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight2);
        setCellStyle(pdfPCell, "right");
        title2.addCell(pdfPCell);



        PdfPTable mergeTable2 = new PdfPTable(6);
        pdfPCell = new PdfPCell(new Paragraph("扣除", getPdfChineseFont()));
        pdfPCell.setColspan(6);
        setCellStyle(pdfPCell, "");
        pdfPCell.setFixedHeight(lineHeight1);
        mergeTable2.addCell(pdfPCell);


        pdfPCell = new PdfPCell(new Paragraph("医药", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("水电房", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("所得税", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("保险", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("公积金", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight2);
        setCellStyle(pdfPCell,"right");
        mergeTable2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("其它", getPdfChineseFont()));
        setCellStyle(pdfPCell, "");
        pdfPCell.setFixedHeight(lineHeight2);
        mergeTable2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(mergeTable2);
        title2.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("实发工资", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight1 + lineHeight2);
        setCellStyle(pdfPCell,"right");
        title2.addCell(pdfPCell);

        title2.setLockedWidth(true);
        title2.setTotalWidth(820);
        title2.setHorizontalAlignment(Element.ALIGN_LEFT);
        title2.setWidths(new float[] { 0.03f, 0.05f, 0.04f, 0.05f,  0.05f,  0.05f, 0.3598f
               , 0.06f, 0.3084f, 0.06f
        });// 单元格宽度
//        title2.setWidths(new float[] { 0.03f, 0.03f, 0.04f, 0.05f,  0.05f,  0.05f,
//                0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f
//        });
        document.add(title2);
    }

    public static Font getPdfChineseFont() throws Exception { // 字体格式设置
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);
        fontChinese.setColor(BaseColor.BLACK);
        fontChinese.setSize(8);
        return fontChinese;
    }


    static PdfPCell setCellStyle(PdfPCell cell, String des) { // 单元格样式设置
        //			设置单元格样式

//        cell.setMinimumHeight(35);
        cell.setMinimumHeight(14f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(1);
        switch (des) {
            case "lr":
                cell.setBorderWidthLeft(0.05f);
                cell.setBorderWidthRight(0.05f);
                cell.setBorderWidthBottom(0.05f);
                return cell;
            case "left":
                cell.setBorderWidthLeft(0.05f);
                cell.setBorderWidthBottom(0.05f);
                return cell;
            case "right":
                cell.setBorderWidthRight(0.05f);
                cell.setBorderWidthBottom(0.05f);
                return cell;
            case "lrb":
                cell.setBorderWidthLeft(0.05f);
                cell.setBorderWidthRight(0.05f);
                cell.setBorderWidthBottom(0.05f);
                return cell;
            case "rb":
                cell.setBorderWidthRight(0.05f);
                cell.setBorderWidthBottom(0.05f);
                return cell;
            default:
                return cell;
        }
//        cell.setBorderWidthTop(0.05f);
//        cell.setBorderWidthBottom(0.05f);
//        cell.setBorderWidthLeft(0.05f);
//        cell.setBorderWidthRight(0.05f);
//        cell.setBorderColorBottom(BaseColor.BLACK);
//        cell.setBorderColorLeft(BaseColor.BLACK);
//        cell.setBorderColorRight(BaseColor.BLACK);
//        cell.setBorderColorTop(BaseColor.BLACK);
//        cell.setPadding(3);
//        return cell;
    }

    static PdfPTable setTableStyle(PdfPTable table) throws DocumentException {
//			设置表格样式
        table.setLockedWidth(true);
        table.setTotalWidth(820);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        // 加起来要等与0.1
        table.setWidths(new float[] { 0.03f, 0.05f, 0.04f, 0.05f,  0.05f,  0.05f,
                0.0514f, 0.0514f, 0.0514f, 0.0514f, 0.0514f, 0.0514f, 0.0514f, 0.06f, 0.0514f, 0.0514f, 0.0514f, 0.0514f, 0.0514f, 0.0514f, 0.06f
        });// 单元格宽度
        return table;

    }


    static SwmWageSummaryFile doSumWage(SwmWageSummaryFile swmWageSummaryFile, SwmWageSummaryFile target){
          swmWageSummaryFile.setPostSkillSalary(swmWageSummaryFile.getPostSkillSalary() == null ? new BigDecimal(0).add(null != target.getPostSkillSalary() ? target.getPostSkillSalary() : new BigDecimal(0))
                  : swmWageSummaryFile.getPostSkillSalary().add(null != target.getPostSkillSalary() ? target.getPostSkillSalary() : new BigDecimal(0)));

          swmWageSummaryFile.setOvertimePay(swmWageSummaryFile.getOvertimePay() == null ? new BigDecimal(0).add(null != target.getOvertimePay() ? target.getOvertimePay() : new BigDecimal(0)) :
                  swmWageSummaryFile.getOvertimePay().add(null != target.getOvertimePay() ? target.getOvertimePay() : new BigDecimal(0)));

          swmWageSummaryFile.setAllocatePerformancePay(swmWageSummaryFile.getAllocatePerformancePay() == null ? new BigDecimal(0).add(null != target.getAllocatePerformancePay() ? target.getAllocatePerformancePay() : new BigDecimal(0)) :
                  swmWageSummaryFile.getAllocatePerformancePay().add(null != target.getAllocatePerformancePay() ? target.getAllocatePerformancePay() : new BigDecimal(0)));

          swmWageSummaryFile.setOneChildSubsidy(swmWageSummaryFile.getOneChildSubsidy() == null ? new BigDecimal(0).add(null != target.getOneChildSubsidy() ? target.getOneChildSubsidy() : new BigDecimal(0)) :
                  swmWageSummaryFile.getOneChildSubsidy().add(null != target.getOneChildSubsidy() ? target.getOneChildSubsidy() : new BigDecimal(0)));

        swmWageSummaryFile.setSafetyAccumulationAward(swmWageSummaryFile.getSafetyAccumulationAward() == null ? new BigDecimal(0).add(null != target.getSafetyAccumulationAward() ? target.getSafetyAccumulationAward() : new BigDecimal(0)) :
                swmWageSummaryFile.getSafetyAccumulationAward().add(null != target.getSafetyAccumulationAward() ? target.getSafetyAccumulationAward() : new BigDecimal(0)));

        swmWageSummaryFile.setAllowanceDeduction(swmWageSummaryFile.getAllowanceDeduction() == null ? new BigDecimal(0).add(null != target.getAllowanceDeduction() ? target.getAllowanceDeduction() : new BigDecimal(0)) :
                swmWageSummaryFile.getAllowanceDeduction().add(null != target.getAllowanceDeduction() ? target.getAllowanceDeduction() : new BigDecimal(0)));

        swmWageSummaryFile.setSeniorityAllowance(swmWageSummaryFile.getSeniorityAllowance() == null ? new BigDecimal(0).add(null != target.getSeniorityAllowance() ? target.getSeniorityAllowance() : new BigDecimal(0)) :
                swmWageSummaryFile.getSeniorityAllowance().add(null != target.getSeniorityAllowance() ? target.getSeniorityAllowance() : new BigDecimal(0)));

        swmWageSummaryFile.setHighTemperatureSubsidy(swmWageSummaryFile.getHighTemperatureSubsidy() == null ? new BigDecimal(0).add(null != target.getHighTemperatureSubsidy() ? target.getHighTemperatureSubsidy() : new BigDecimal(0)) :
                swmWageSummaryFile.getHighTemperatureSubsidy().add(null != target.getHighTemperatureSubsidy() ? target.getHighTemperatureSubsidy() : new BigDecimal(0)));

        swmWageSummaryFile.setTransportationAllowance(swmWageSummaryFile.getTransportationAllowance() == null ? new BigDecimal(0).add(null != target.getTransportationAllowance() ? target.getTransportationAllowance() : new BigDecimal(0)) :
                swmWageSummaryFile.getTransportationAllowance().add(null != target.getTransportationAllowance() ? target.getTransportationAllowance() : new BigDecimal(0)));

        swmWageSummaryFile.setPostSubsidy(swmWageSummaryFile.getPostSubsidy() == null ? new BigDecimal(0).add(null != target.getPostSubsidy() ? target.getPostSubsidy() : new BigDecimal(0)) :
                swmWageSummaryFile.getPostSubsidy().add(null != target.getPostSubsidy() ? target.getPostSubsidy() : new BigDecimal(0)));

        swmWageSummaryFile.setWagesPayable(swmWageSummaryFile.getWagesPayable() == null ? new BigDecimal(0).add(null != target.getWagesPayable() ? target.getWagesPayable() : new BigDecimal(0)) :
                swmWageSummaryFile.getWagesPayable().add(null != target.getWagesPayable() ? target.getWagesPayable() : new BigDecimal(0)));

        swmWageSummaryFile.setDeductMedicalCosts(swmWageSummaryFile.getDeductMedicalCosts() == null ? new BigDecimal(0).add(null != target.getDeductMedicalCosts() ? target.getDeductMedicalCosts() : new BigDecimal(0)) :
                swmWageSummaryFile.getDeductMedicalCosts().add(null != target.getDeductMedicalCosts() ? target.getDeductMedicalCosts() : new BigDecimal(0)));

        swmWageSummaryFile.setDeductHydropowerHouse(swmWageSummaryFile.getDeductHydropowerHouse() == null ? new BigDecimal(0).add(null != target.getDeductHydropowerHouse() ? target.getDeductHydropowerHouse() : new BigDecimal(0)) :
                swmWageSummaryFile.getDeductHydropowerHouse().add(null != target.getDeductHydropowerHouse() ? target.getDeductHydropowerHouse() : new BigDecimal(0)));

        swmWageSummaryFile.setDeductIncomeTax(swmWageSummaryFile.getDeductIncomeTax() == null ? new BigDecimal(0).add(null != target.getDeductIncomeTax() ? target.getDeductIncomeTax() : new BigDecimal(0)) :
                swmWageSummaryFile.getDeductIncomeTax().add(null != target.getDeductIncomeTax() ? target.getDeductIncomeTax() : new BigDecimal(0)));

        swmWageSummaryFile.setPersonalDeductibles(swmWageSummaryFile.getPersonalDeductibles() == null ? new BigDecimal(0).add(null != target.getPersonalDeductibles() ? target.getPersonalDeductibles() : new BigDecimal(0)) :
                swmWageSummaryFile.getPersonalDeductibles().add(null != target.getPersonalDeductibles() ? target.getPersonalDeductibles() : new BigDecimal(0)));

        swmWageSummaryFile.setPersonalDeductAccumulationFund(swmWageSummaryFile.getPersonalDeductAccumulationFund() == null ? new BigDecimal(0).add(null != target.getPersonalDeductAccumulationFund() ? target.getPersonalDeductAccumulationFund() : new BigDecimal(0)) :
                swmWageSummaryFile.getPersonalDeductAccumulationFund().add(null != target.getPersonalDeductAccumulationFund() ? target.getPersonalDeductAccumulationFund() : new BigDecimal(0)));

        swmWageSummaryFile.setDeductOther(swmWageSummaryFile.getDeductOther() == null ? new BigDecimal(0).add(null != target.getDeductOther() ? target.getDeductOther() : new BigDecimal(0)) :
                swmWageSummaryFile.getDeductOther().add(null != target.getDeductOther() ? target.getDeductOther() : new BigDecimal(0)));

        swmWageSummaryFile.setNetPayment(swmWageSummaryFile.getNetPayment() == null ? new BigDecimal(0).add(null != target.getNetPayment() ? target.getNetPayment() : new BigDecimal(0)) :
                swmWageSummaryFile.getNetPayment().add(null != target.getNetPayment() ? target.getNetPayment() : new BigDecimal(0)));

          return swmWageSummaryFile;
    }


    private void bonusPaymentTableAddCell(PdfPTable table, SwmBonusPayment swmBonusPayment, Integer count) throws Exception {
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(count.toString(), getPdfChineseFont())), "lr"));
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmBonusPayment.getEmployeeName(), getPdfChineseFont())), "right"));
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmBonusPayment.getBankAccount(), getPdfChineseFont())),"right"));
        setBonusPaymentCell(table, swmBonusPayment, false);
    }

    private void setBonusPaymentCell(PdfPTable table, SwmBonusPayment swmBonusPayment, Boolean lastRowFlag) throws Exception {
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmBonusPayment.getPayableAmount() != null ? swmBonusPayment.getPayableAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), lastRowFlag ? "rb" : "right"));
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmBonusPayment.getAmountPreTax() != null ? swmBonusPayment.getAmountPreTax().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "0.00", getPdfChineseFont())), lastRowFlag ? "rb" : "right"));
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmBonusPayment.getDeductIncomeTax() != null && !swmBonusPayment.getDeductIncomeTax().equals(new BigDecimal(0)) ? swmBonusPayment.getDeductIncomeTax().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "", getPdfChineseFont())), lastRowFlag ? "rb" : "right"));
        table.addCell(setCellStyle(new PdfPCell(new Paragraph(swmBonusPayment.getAmountAfterTax() != null && !swmBonusPayment.getDeductIncomeTax().equals(new BigDecimal(0)) ? swmBonusPayment.getAmountAfterTax().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "", getPdfChineseFont())), lastRowFlag ? "rb" : "right"));
    }

    private void bonusPaymentAddCell(PdfPTable table, SwmBonusPayment swmBonusPayment,Boolean totalFlag, Boolean lastRowFlag) throws  Exception { // 奖金发放科室小计
        table.addCell(setCellStyle(new PdfPCell(new Paragraph("")), lastRowFlag ? "lrb" : "lr"));
        if (!totalFlag) {
            table.addCell(setCellStyle(new PdfPCell(new Paragraph("科室小计", getPdfChineseFont())), lastRowFlag ? "rb" : "right"));
        } else  {
            table.addCell(setCellStyle(new PdfPCell(new Paragraph("部门合计", getPdfChineseFont())), lastRowFlag ? "rb": "right"));
        }
        table.addCell(setCellStyle(new PdfPCell(new Paragraph("")), lastRowFlag ? "rb": "right"));
        setBonusPaymentCell(table, swmBonusPayment, lastRowFlag);
    }

    private void sumBonusPaymentAddCell(PdfPTable table, SwmBonusPayment swmBonusPayment) throws Exception { // 奖金发放合计
        PdfPCell pdfPCell;
        pdfPCell = new PdfPCell(new Paragraph("合计", getPdfChineseFont()));
        pdfPCell.setColspan(3);
        table.addCell(setCellStyle(pdfPCell, "right"));
        setBonusPaymentCell(table, swmBonusPayment, true);
    }




    static PdfPTable setSwmBonusPaymentStyle(PdfPTable table) throws DocumentException {
//			设置表格样式
        table.setLockedWidth(true);
        table.setTotalWidth(580);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        // 单元格宽度
        table.setWidths(new float[] {
            0.05f, 0.1f , 0.25f, 0.15f, 0.15f, 0.15f, 0.15f
        });
        return table;

    }

    private void generateBonusPaymentHeader(Document document, float lineHeight) throws Exception {
        PdfPCell pdfPCell;
        PdfPTable title = new PdfPTable(7);
        title.setSpacingBefore(5f);
        pdfPCell = new PdfPCell(new Paragraph("序号", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight);
        setCellStyle(pdfPCell, "lr");
        title.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("姓名", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight);
        setCellStyle(pdfPCell, "right");
        title.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("银行账号", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight);
        setCellStyle(pdfPCell, "right");
        title.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("应发奖金", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight);
        setCellStyle(pdfPCell, "right");
        title.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("实发奖金（税前）", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight);
        setCellStyle(pdfPCell, "right");
        title.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("双薪福利所得税", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight);
        setCellStyle(pdfPCell, "right");
        title.addCell(pdfPCell);

        pdfPCell = new PdfPCell(new Paragraph("实发奖金（税后）", getPdfChineseFont()));
        pdfPCell.setFixedHeight(lineHeight);
        setCellStyle(pdfPCell, "right");
        title.addCell(pdfPCell);
        title.setLockedWidth(true);
        title.setTotalWidth(580);
        title.setHorizontalAlignment(Element.ALIGN_LEFT);
        title.setWidths(new float[] {
                0.05f, 0.1f , 0.25f, 0.15f, 0.15f, 0.15f, 0.15f
        });
        document.add(title);

    }

    static SwmBonusPayment doSumBonusPayment(SwmBonusPayment swmBonusPayment, SwmBonusPayment target) {
        swmBonusPayment.setPayableAmount(swmBonusPayment.getPayableAmount() == null ? new BigDecimal(0).add(null != target.getPayableAmount() ? target.getPayableAmount() : new BigDecimal(0))
                : swmBonusPayment.getPayableAmount().add(null != target.getPayableAmount() ? target.getPayableAmount() : new BigDecimal(0))
        );
        swmBonusPayment.setAmountPreTax(swmBonusPayment.getAmountPreTax() == null ? new BigDecimal(0).add(null != target.getAmountPreTax() ? target.getAmountPreTax() : new BigDecimal(0))
                : swmBonusPayment.getAmountPreTax().add(null != target.getAmountPreTax() ? target.getAmountPreTax() : new BigDecimal(0))
        );
        swmBonusPayment.setDeductIncomeTax(swmBonusPayment.getDeductIncomeTax() == null ? new BigDecimal(0).add(null != target.getDeductIncomeTax() ? target.getDeductIncomeTax() : new BigDecimal(0))
                : swmBonusPayment.getDeductIncomeTax().add(null != target.getDeductIncomeTax() ? target.getDeductIncomeTax() : new BigDecimal(0))
        );
        swmBonusPayment.setAmountAfterTax(swmBonusPayment.getAmountAfterTax() == null ? new BigDecimal(0).add(null != target.getAmountAfterTax() ? target.getAmountAfterTax() : new BigDecimal(0))
                : swmBonusPayment.getAmountAfterTax().add(null != target.getAmountAfterTax() ? target.getAmountAfterTax() : new BigDecimal(0))
        );
        return swmBonusPayment;
    }
}
