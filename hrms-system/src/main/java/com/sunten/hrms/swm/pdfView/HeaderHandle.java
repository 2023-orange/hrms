package com.sunten.hrms.swm.pdfView;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.sunten.hrms.fnd.dao.FndDictDetailDao;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.service.impl.FndUserServiceImpl;
import com.sunten.hrms.swm.dao.SwmFloatingWageDao;
import com.sunten.hrms.swm.dao.SwmPostSkillSalaryDao;
import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.mapper.SwmPostSkillSalaryMapper;
import com.sunten.hrms.utils.DateUtil;
import com.sunten.hrms.utils.SecurityUtils;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HeaderHandle extends PdfPageEventHelper {

    private  int presentFontSize = 10;

    private PdfTemplate total;

    BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
            false);
    Font fontPage = new Font(bfChinese, 10, Font.NORMAL);

    Font fontEnd = new Font(bfChinese, 10, Font.BOLD);

    Font title = new Font(bfChinese, 12, Font.BOLD);

    private String empName;

    private String headerName;

    private SwmBonus swmBonus; // 奖金发放表专用

    /**
     *  @author：liangjw
     *  @Date: 2021/3/10 15:41
     *  @Description:
     *  @params: empName 制表人 headerName 报表名 swmBonus 出奖金发放表专用
     */
    public HeaderHandle(String empName, String headerName, SwmBonus swmBonus) throws IOException, DocumentException {
        this.empName = empName;
        this.headerName = headerName;
        this.swmBonus = swmBonus;
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(60, 80);// 共 页 的矩形的长宽高
        total.setWidth(50);
    }


//
//    @SneakyThrows
//    @Override
//    public void onEndPage(PdfWriter writer, Document document) {
//
//        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
//                BaseFont.NOT_EMBEDDED);
//        Font fontPage = new Font(bfChinese, 9, Font.NORMAL);
//        fontPage.setColor(BaseColor.BLACK);
//        fontPage.setSize(9);
//        //添加标题文本
//        StringBuffer underline = new StringBuffer();
//        for(int i = 0;i<116;i++) {
//            underline.append("_");
//        }
//        Phrase contentPh = new Phrase("这是是页眉",fontPage);
//        Phrase underlinePh = new Phrase(underline.toString(),fontPage);
//        Phrase pageNumberPh = new Phrase("第 " + document.getPageNumber() + " 页    共 " + document.getPageSize() + " 页",fontPage);
//        float center = document.getPageSize().getRight() - 200;//页面的水平中点
//        float top = document.getPageSize().getTop()-36;
//        float bottom = document.getPageSize().getBottom()+36;
//
//        /** 参数xy是指文本显示的页面上的哪个店。alignment指文本在坐标点的对齐方式 */
////        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,contentPh,center,top,0); //页眉
////        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,underlinePh,center,top-3,0); //页眉
////        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,pageNumberPh,center,bottom,0); //页码
//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,pageNumberPh,center,top,0); //页眉
//    }
    @SneakyThrows
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        float center = document.getPageSize().getRight() - 200;//页面的水平中点

        // 1.写入页眉
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(headerName, title), document.getPageSize().getRight() / 2, document.top() + 20, 0);

        // 2.写入前半部分的 第 X页/共
        int pageS = writer.getPageNumber();
        String foot1 = "第  " + pageS + "  页  /  " + total;
        Phrase footer = new Phrase(foot1, fontPage);
        Phrase bonusNameFooter = new Phrase();
        Phrase bonusReleaseTimeFooter = new Phrase();
        Phrase bonusMoneyFooter = new Phrase();

        if (null != swmBonus) {
            String bonusName = "奖金名称：" + swmBonus.getBonusName();
            String bonusReleaseTime = swmBonus.getReleaseTime();
            String bonusMoney =  "金额:" + (null == swmBonus.getMoney() ? "" : swmBonus.getMoney());
            bonusNameFooter = new Phrase(bonusName ,fontPage);
            bonusReleaseTimeFooter = new Phrase(bonusReleaseTime ,fontPage);
            bonusMoneyFooter = new Phrase(bonusMoney, fontPage);
        }


        PdfContentByte cb = writer.getDirectContent();


        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日");

        if (!bonusNameFooter.getContent().equals("") && !bonusReleaseTimeFooter.getContent().equals("")) {
            //奖金名称眉
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,bonusNameFooter,document.getPageSize().getLeft() + 5,document.top() + 10,0);
            //发放时间眉
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,bonusReleaseTimeFooter, center,document.top() + 10,0);
            //发放金额
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,bonusMoneyFooter, center + 50,document.top() + 10,0);
            //黑色实线
            LineSeparator line = new LineSeparator(1f,100,BaseColor.BLACK,Element.ALIGN_CENTER,-5f);
//            LineSeparator lineSeparator = new LineSeparator(document.getPageSize().getWidth(), 0.5f, BaseColor.BLACK,Element.ALIGN_CENTER, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,new Phrase(new Chunk(line)), 0,document.top() + 20,0);
        }

        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,footer,center + 100,document.top() + 7,0); //页眉
        if (!headerName.contains("顺特电气设备工资发放表")) {
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("制表人: " + empName, fontEnd), document.getPageSize().getRight() / 4 - 140, document.bottom() - 20, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("审核:", fontEnd), document.getPageSize().getRight() / 2 - 180, document.bottom() - 20, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("财务总监:", fontEnd), document.getPageSize().getRight() / 2 - 20, document.bottom() - 20, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("总经理:", fontEnd), document.getPageSize().getRight() / 2 + 150, document.bottom() - 20, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(LocalDate.now().format(fmt), fontEnd), document.getPageSize().getRight() - 90, document.bottom() - 20, 0);
        }
            // 页脚

            // 6.写入页脚2的模板（就是页脚的Y页这俩字）添加到文档中，计算模板的和Y轴,X=(右边界-左边界 - 前半部分的len值)/2.0F + len ， y 轴和之前的保持一致，底边界-20
            cb.addTemplate(total, center + 125, document.top() + 7); // 调节模版显示的位置
    }

    /**
     *
     * TODO 关闭文档时，替换模板，完成整个页眉页脚组件
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    @SneakyThrows
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // 7.最后一步了，就是关闭文档的时候，将模板替换成实际的 Y 值,至此，page x of y 制作完毕，完美兼容各种文档size。
        total.beginText();
        total.setFontAndSize(bfChinese, presentFontSize);// 生成的模版的字体、颜色
        String foot2 = "共  " + (writer.getPageNumber() - 1) + "  页  ";
        total.showText(foot2);// 模版显示的内容
        total.endText();
        total.closePath();
    }
}
