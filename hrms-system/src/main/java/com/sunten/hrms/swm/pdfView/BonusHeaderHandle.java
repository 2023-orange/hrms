package com.sunten.hrms.swm.pdfView;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.sunten.hrms.swm.domain.SwmBonus;
import lombok.SneakyThrows;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BonusHeaderHandle extends PdfPageEventHelper {
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

    public BonusHeaderHandle(String empName, String headerName, SwmBonus swmBonus) throws IOException, DocumentException {
        this.empName = empName;
        this.headerName = headerName;
        this.swmBonus = swmBonus;
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(60, 80);// 共 页 的矩形的长宽高
        total.setWidth(50);
    }
    @SneakyThrows
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        float center = document.getPageSize().getRight() - 200;

        // 1.写入页眉
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(headerName, title), document.getPageSize().getRight() / 2, document.top() + 30, 0);

        // 2.写入前半部分的 第 X页/共
        int pageS = writer.getPageNumber();
        String foot = "第  " + pageS + "  页  /  " + total;
        Phrase footer = new Phrase(foot, fontPage);
        String bonusName = "奖金名称：" + swmBonus.getBonusName();
        String releaseTime = "发放时间：" + swmBonus.getReleaseTime();
        Phrase bonusNameFooter = new Phrase(bonusName, fontPage);
        Phrase bonusReleaseTimeFooter = new Phrase(releaseTime, fontPage);

        PdfContentByte cb = writer.getDirectContent();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日");
        if (null == swmBonus.getMoney() || swmBonus.getMoney().compareTo(new BigDecimal(0)) == 0) {
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,bonusReleaseTimeFooter, document.getPageSize().getLeft() + 300,document.top() + 10,0);
        } else {
            String bonusMoney = "金额:" + (null == swmBonus.getMoney() ? "" : swmBonus.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            Phrase bonusMoneyFooter = new Phrase(bonusMoney, fontPage);
            //发放金额
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,bonusMoneyFooter, document.getPageSize().getLeft() + 380,document.top() + 10,0);
            //发放时间眉
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,bonusReleaseTimeFooter, document.getPageSize().getLeft() + 235,document.top() + 10,0);
        }






        //奖金名称眉
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,bonusNameFooter,document.getPageSize().getLeft() + 75,document.top() + 10,0);

        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,footer,document.getPageSize().getRight() - 80,document.top() + 10,0); //页眉
        //黑色实线
//        LineSeparator lineSeparator = new LineSeparator(document.getPageSize().getWidth(), 0.5f, BaseColor.BLACK,Element.ALIGN_CENTER, 0);
//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER,new Phrase(new Chunk(lineSeparator)), center,document.top() + 20,0);

//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("制表人: " + empName, fontEnd), document.getPageSize().getRight() / 4 - 70 ,document.bottom() - 15, 0);
//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("审核:" , fontEnd), document.getPageSize().getRight() / 2 - 120 ,document.bottom() - 15, 0);
//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("财务总监:" , fontEnd), document.getPageSize().getRight() / 2 - 10 ,document.bottom() -15, 0);
//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("批准:" , fontEnd), document.getPageSize().getRight() / 2 + 90 ,document.bottom() - 15, 0);
//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase(LocalDate.now().format(fmt), fontEnd), document.getPageSize().getRight()  - 65 ,document.bottom() - 15, 0);
        // 页脚

        // 6.写入页脚2的模板（就是页脚的Y页这俩字）添加到文档中，计算模板的和Y轴,X=(右边界-左边界 - 前半部分的len值)/2.0F + len ， y 轴和之前的保持一致，底边界-20
        cb.addTemplate(total, document.getPageSize().getRight() - 60, document.top() + 10); // 调节模版显示的位置
    }

    /**
     *
     *
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
