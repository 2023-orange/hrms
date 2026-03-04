package com.sunten.hrms.utils;

import cn.hutool.core.io.FileUtil;
import com.sunten.hrms.utils.enums.CamundaTag;
import com.sunten.hrms.utils.enums.NamespaceName;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.AbstractAttribute;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.List;

/**
 * 作者 CG
 * 时间 2021/4/12 14:21
 * 备注 camunda 和 activiti xml 互相转换
 * 参数
 */
public class ConversionUtil {
    public static void main(String[] args) {
        File file1 = new File("E:\\idea_workspace\\SUNTEN_OA\\src\\diagrams\\ReqProbationAssess.bpmn");
        camundaToActiviti(file1,false);
    }

    /**
     * 作者 CG
     * 时间 2021/4/12 14:22
     * 备注 判断文件名字是否是xml,不是xml转换为xml
     * 参数
     */
    public static File isXml(File file) {
//        判断是否为null
        String name = FileUtil.mainName(file);
        String parent = file.getParent();
        boolean bpmn = FileUtil.pathEndsWith(file, "bpmn");
        if (bpmn) {
            //转换为xml
            File xmlFile = FileUtil.file(parent, name + ".xml");
            File copy = FileUtil.copy(file, xmlFile, true);
            return copy;
        }
        return file;
    }
    /**
     * 作者 CG
     * 时间 2021/4/12 15:57
     * 备注 属性转换
     * 参数 [file, flag camunda->activiti=true  activiti->camunda=false]
     */
    public static void camundaToActiviti(File file, Boolean flag) {
        //判断文件后缀名是否正确
        try {
            File xml = isXml(file);
            //转换为文档
            SAXReader reader = new SAXReader();
//       转换为Document
            Document doc = reader.read(xml);
            //获取根节点
            Element rootElement = doc.getRootElement();
            //获取命名空间
            //flag camunda->activiti=true  activiti->camunda=false
            NamespaceHandler(rootElement, flag);
            //处理userTask
            Element process = rootElement.element("process");
            List<Element> userTask = process.elements("userTask");
            CamundaTag[] values = CamundaTag.values();
            if (!CollectionUtils.isEmpty(userTask)) {
                for (CamundaTag value : values) {
                    userTask(userTask, value, flag);
                }
                //处理监听
                userTaskListener(userTask, flag);
            }
            //保存文档
            saveDocument(doc, file);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 作者 CG
     * 时间 2021/4/12 17:45
     * 备注 处理命名空间
     * 参数 [rootElement, flag]
     */
    public static void NamespaceHandler(Element rootElement, Boolean flag) {
        NamespaceName camundaNamespace = NamespaceName.camunda;
        NamespaceName activitiNamespace = NamespaceName.activiti;
        Namespace activiti = rootElement.getNamespaceForPrefix(flag ? camundaNamespace.getName() : activitiNamespace.getName());
        if (activiti != null) {
            //删除camunda命名空间
            rootElement.remove(activiti);
            //添加activiti命名空间
            Namespace namespace = new Namespace(flag ? activitiNamespace.getName() : camundaNamespace.getName(), flag ? activitiNamespace.getUrl() : camundaNamespace.getUrl());
            rootElement.add(namespace);
        }
    }

    /**
     * 作者 CG
     * 时间 2021/4/12 15:36
     * 备注 userTask 参数转换
     * 参数 [userTask 要处理的集合, camundaTag 要处理的参数类型, flag camunda->activiti=true  activiti->camunda=false]
     */
    public static void userTask(List<Element> userTask, final CamundaTag camundaTag, final Boolean flag) {
        for (Element element : userTask) {
            final Attribute assignee = element.attribute(camundaTag.getAttribute());
            if (assignee != null) {
                element.remove(assignee);
                AbstractAttribute abstractAttribute = new AbstractAttribute() {
                    @Override
                    public QName getQName() {
                        QName qName = new QName(flag ? camundaTag.getActivitiName() : camundaTag.getCamundaName());
                        return qName;
                    }

                    @Override
                    public String getValue() {
                        return assignee.getValue();
                    }
                };
                element.add(abstractAttribute);
            }
        }
    }
    /**
     * 作者 CG
     * 时间 2021/4/12 15:36
     * 备注 userTask 参数转换
     * 参数 [userTask 要处理的集合, camundaTag 要处理的参数类型, flag camunda->activiti=true  activiti->camunda=false]
     */
    public static void userTaskListener(List<Element> userTask, Boolean flag) {
        CamundaTag listener = CamundaTag.taskListener;
        for (Element element : userTask) {
            Element extensionElements = element.element("extensionElements");
            if (extensionElements!=null){
                //是否有监听
                Element taskListener = extensionElements.element(listener.getAttribute());
                if (taskListener!=null){
                    //获取节点所有参数
                    List<Attribute> attributes = taskListener.attributes();
                    DefaultElement defaultElement = new DefaultElement(flag?listener.getActivitiName():listener.getCamundaName());
                    //将参数拷贝到新的节点
                    for (Attribute attribute : attributes) {
                        DefaultAttribute defaultAttribute = new DefaultAttribute(attribute.getName(), attribute.getValue());
                        defaultElement.add(defaultAttribute);
                    }
                    //删除原有节点
                    extensionElements.remove(taskListener);
                    //添加新构建的节点
                    extensionElements.add(defaultElement);
                }

            }
        }
    }
    /**
     * 作者 CG
     * 时间 2021/4/12 15:45
     * 备注 文档保存
     * 参数 [document, xmlFile]
     */
    public static void saveDocument(Document document, File xmlFile) throws IOException {
        Writer osWrite = new OutputStreamWriter(new FileOutputStream(xmlFile));//创建输出流
        OutputFormat format = OutputFormat.createPrettyPrint();  //获取输出的指定格式
        format.setEncoding("UTF-8");//设置编码 ，确保解析的xml为UTF-8格式
        XMLWriter writer = new XMLWriter(osWrite, format);//XMLWriter 指定输出文件以及格式
        writer.write(document);//把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)
        writer.flush();
        writer.close();
    }
}

