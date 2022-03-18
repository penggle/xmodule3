package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.util.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通用对象树Builder
 * 
 * @param <I>
 * @param <T>
 * @author 	pengpeng
 * @version 	1.0
 */
public abstract class ObjectTreeBuilder<I,T extends Comparable<T>> {

	private final Integer defaultRootLevel;

	public ObjectTreeBuilder() {
		this(0);
	}

	public ObjectTreeBuilder(Integer defaultRootLevel) {
		this.defaultRootLevel = defaultRootLevel;
	}

	/**
	 * 根据parentTreeNodeId及全部树节点列表来渲染一颗具有父子关系的对象树
	 * parentTreeNodeId在一般情况下它应该是一个根节点的父节点ID
	 *
	 * @param parentTreeNodeIds		- 父节点ID列表
	 * @param allTreeNodeList		- 需要构造对象树的所有节点列表
	 * @return
	 */
	public List<T> buildObjectTree(List<I> parentTreeNodeIds, List<T> allTreeNodeList) {
		Assert.notEmpty(parentTreeNodeIds, "Parameter 'parentTreeNodeIds' can not be empty!");
		Assert.notEmpty(allTreeNodeList, "Parameter 'allTreeNodeList' can not be empty!");
		//1、找顶层节点列表
		List<T> parentTreeNodeList = new ArrayList<>();
		for(I parentNodeId : parentTreeNodeIds) {
			T parentNode = getTreeNodeById(parentNodeId, allTreeNodeList);
			if(parentNode != null) { //parentNodeId对应的TreeNode不存在?
				parentTreeNodeList.add(parentNode);
			} else {
				//走到这里说明parentTreeNodeIds是根节点
				List<T> rootTreeNodeList = getDirectChildNodeList(parentNodeId, allTreeNodeList);
				if(!CollectionUtils.isEmpty(rootTreeNodeList)) {
					parentTreeNodeList.addAll(rootTreeNodeList);
				}
			}
		}
		//2、加载下层节点列表
		if(!CollectionUtils.isEmpty(parentTreeNodeList)) {
			for(T parentNode : parentTreeNodeList) {
				recursiveLoadChildTreeNodes(parentNode, allTreeNodeList, getDefaultRootLevel());
			}
		}
		return parentTreeNodeList;
	}
	
	/**
	 * 根据parentTreeNodeId及全部树节点列表来渲染一颗具有父子关系的对象树，同时提供对树节点数据类型的转换
	 * parentTreeNodeId在一般情况下它应该是一个根节点的父节点ID
	 *
	 * @param parentTreeNodeIds		- 父节点ID列表
	 * @param allTreeNodeList		- 需要构造对象树的所有节点列表
	 * @param treeNodeConverter		- 树节点数据结构转换器
	 * @return
	 */
	public <R> List<R> buildObjectTree(List<I> parentTreeNodeIds, List<T> allTreeNodeList, TreeNodeConverter<T,R> treeNodeConverter) {
		Assert.notEmpty(parentTreeNodeIds, "Parameter 'parentTreeNodeIds' can not be empty!");
		Assert.notEmpty(allTreeNodeList, "Parameter 'allTreeNodeList' can not be empty!");
		Assert.notNull(treeNodeConverter, "Parameter 'treeNodeConverter' can not be null!");
		List<T> sourceTreeNodeList = buildObjectTree(parentTreeNodeIds, allTreeNodeList);
		List<R> targetTreeNodeList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(sourceTreeNodeList)){
			for(T sourceTreeNode : sourceTreeNodeList){
				R targetTreeNode = treeNodeConverter.convertTreeNode(sourceTreeNode);
				targetTreeNodeList.add(targetTreeNode);
				recursiveConvertTreeNode(sourceTreeNode, targetTreeNode, treeNodeConverter);
			}
		}
		return targetTreeNodeList;
	}

	/**
	 * 获取指定parentTreeNodeId的直接子节点列表
	 *
	 * @param parentTreeNodeId	- 父节点ID
	 * @param allTreeNodeList	- 所有节点列表
	 * @return
	 */
	public List<T> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList) {
		List<T> childTreeNodeList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(allTreeNodeList) && !ObjectUtils.isEmpty(parentTreeNodeId)){
			for(T treeNode : allTreeNodeList){
				if(treeNode != null && parentTreeNodeId.equals(getParentTreeNodeId(treeNode))){
					childTreeNodeList.add(treeNode);
				}
			}
		}
		return childTreeNodeList;
	}

	/**
	 * 获取指定parentNodeId的直接子节点列表，同时提供对树节点数据类型的转换
	 *
	 * @param parentTreeNodeId		- 父节点ID
	 * @param allTreeNodeList		- 所有节点列表
	 * @param treeNodeConverter		- 树节点数据转换器
	 * @param <R>
	 * @return
	 */
	public <R> List<R> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList, TreeNodeConverter<T,R> treeNodeConverter) {
		List<R> childTreeNodeList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(allTreeNodeList) && !ObjectUtils.isEmpty(parentTreeNodeId) && treeNodeConverter != null){
			for(T treeNode : allTreeNodeList){
				if(treeNode != null && parentTreeNodeId.equals(getParentTreeNodeId(treeNode))){
					childTreeNodeList.add(treeNodeConverter.convertTreeNode(treeNode));
				}
			}
		}
		return childTreeNodeList;
	}

	/**
	 * 递归构转换对象树
	 *
	 * @param sourceTreeNode	- 源节点对象
	 * @param targetTreeNode	- 目标节点对象
	 * @param treeNodeConverter	- 树节点数据转换器
	 * @param <R>
	 */
	public <R> void recursiveConvertTreeNode(T sourceTreeNode, R targetTreeNode, TreeNodeConverter<T, R> treeNodeConverter) {
		if(sourceTreeNode != null && targetTreeNode != null) {
			List<T> sourceChildNodeList = getChildTreeNodeList(sourceTreeNode);
			if(!CollectionUtils.isEmpty(sourceChildNodeList)) {
				List<R> resultTreeNodeChildList = new ArrayList<>();
				for(T sourceChildNode : sourceChildNodeList) {
					R resultTreeNode = treeNodeConverter.convertTreeNode(sourceChildNode);
					resultTreeNodeChildList.add(resultTreeNode);
					recursiveConvertTreeNode(sourceChildNode, resultTreeNode, treeNodeConverter);
				}
				treeNodeConverter.setChildTreeNodeList(targetTreeNode, resultTreeNodeChildList);
			}
		}
	}

	/**
	 * 递归构加载子对象树
	 *
	 * @param currentTreeNode	- 当前树节点
	 * @param allTreeNodeList	- 所有树节点列表
	 * @param level				- 层级值
	 */
	public void recursiveLoadChildTreeNodes(T currentTreeNode, List<T> allTreeNodeList, int level) {
		if(currentTreeNode != null && !CollectionUtils.isEmpty(allTreeNodeList)) {
			setTreeNodeLevel(currentTreeNode, level);
			attachTreeNodePath(currentTreeNode, allTreeNodeList);
			List<T> directChildList = getDirectChildNodeList(getTreeNodeId(currentTreeNode), allTreeNodeList);
			if(!CollectionUtils.isEmpty(directChildList)) {
				Collections.sort(directChildList);
			}
			setChildTreeNodeList(currentTreeNode, directChildList);
			if(!CollectionUtils.isEmpty(directChildList)) {
				for(T childTreeNode : directChildList){
					recursiveLoadChildTreeNodes(childTreeNode, allTreeNodeList, level + 1);
				}
			}
		}
	}

	/**
	 * 附加上树节点的tree-path
	 *
	 * @param currentTreeNode
	 * @param allTreeNodeList
	 */
	protected void attachTreeNodePath(T currentTreeNode, List<T> allTreeNodeList) {
		String parentPath = "";
		I parentTreeNodeId = getParentTreeNodeId(currentTreeNode);
		if(parentTreeNodeId != null){
			T parentTreeNode = getTreeNodeById(parentTreeNodeId, allTreeNodeList);
			if(parentTreeNode != null){
				parentPath = StringUtils.defaultIfEmpty(getTreeNodePath(parentTreeNode), "");
			}
		}
		String currentPath = parentPath + "/" + getTreeNodeId(currentTreeNode);
		setTreeNodePath(currentTreeNode, currentPath);
	}

	/**
	 * 根据treeNodeId获取TreeNode对象
	 *
	 * @param treeNodeId
	 * @param allTreeNodeList
	 * @return
	 */
	protected T getTreeNodeById(I treeNodeId, List<T> allTreeNodeList) {
		if(!CollectionUtils.isEmpty(allTreeNodeList) && !ObjectUtils.isEmpty(treeNodeId)){
			for(T treeNode : allTreeNodeList){
				if(treeNode != null && treeNodeId.equals(getTreeNodeId(treeNode))){
					return treeNode;
				}
			}
		}
		return null;
	}

	protected Integer getDefaultRootLevel() {
		return defaultRootLevel;
	}

	/**
	 * 返回指定treeNode的ID
	 *
	 * @param treeNode
	 * @return
	 */
	protected abstract I getTreeNodeId(T treeNode);

	/**
	 * 返回指定treeNode的父节点ID
	 *
	 * @param treeNode
	 * @return
	 */
	protected abstract I getParentTreeNodeId(T treeNode);

	/**
	 * 设置指定treeNode的直接子节点列表
	 *
	 * @param treeNode
	 * @param directChildList
	 */
	protected abstract void setChildTreeNodeList(T treeNode, List<T> directChildList);

	/**
	 * 返回指定treeNode的直接子节点列表
	 *
	 * @param treeNode
	 * @return
	 */
	protected abstract List<T> getChildTreeNodeList(T treeNode);

	/**
	 * 设置指定treeNode的层级
	 * @param treeNode
	 * @param level
	 */
	protected abstract void setTreeNodeLevel(T treeNode, Integer level);

	/**
	 * 设置指定treeNode的tree-path值
	 * @param treeNode
	 * @param treePath
	 */
	protected abstract void setTreeNodePath(T treeNode, String treePath);

	/**
	 * 返回指定treeNode的tree-path值
	 * @param treeNode
	 * @return
	 */
	protected abstract String getTreeNodePath(T treeNode);


	/**
	 * 树节点数据结构转换器
	 *
	 * @param <T>
	 * @param <R>
	 * @author 	pengpeng
	 * @version 1.0
	 */
	public interface TreeNodeConverter<T,R> {

		R convertTreeNode(T targetTreeNode);

		void setChildTreeNodeList(R resultTreeNode, List<R> subTreeNodeList);

	}

}