package com.sunten.hrms.utils;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.map.MapUtil;
import com.sunten.hrms.domain.ExcelHeadTreeNode;

import java.util.Map;
public class ExcelHeadNodeParser<T> implements NodeParser<ExcelHeadTreeNode<T>, T> {

    @Override
    public void parse(ExcelHeadTreeNode<T> treeNode, Tree<T> tree) {
        tree.setId(treeNode.getId());
        tree.setParentId(treeNode.getParentId());
        tree.setWeight(treeNode.getWeight());
        tree.setName(treeNode.getName());
        tree.putExtra("cellStyle", treeNode.getStyle());

        //扩展字段
        final Map<String, Object> extra = treeNode.getExtra();
        if(MapUtil.isNotEmpty(extra)){
            extra.forEach(tree::putExtra);
        }
    }
}
