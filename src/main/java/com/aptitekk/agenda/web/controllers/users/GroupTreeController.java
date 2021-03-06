package com.aptitekk.agenda.web.controllers.users;

import com.aptitekk.agenda.core.services.UserGroupService;
import com.aptitekk.agenda.core.entity.UserGroup;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.*;

@ManagedBean(name = "GroupTreeController")
@RequestScoped
public class GroupTreeController {

    private HashMap<Integer, TreeNode> cache = new HashMap<>();

    @Inject
    private UserGroupService groupService;

    private TreeNode buildTree(List<UserGroup> currentlySelectedGroups, UserGroup userGroupToExclude, boolean allowRootSelection, boolean readOnly) {
        UserGroup rootGroup = groupService.getRootGroup();

        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode rootNode = new DefaultTreeNode(rootGroup);
        rootNode.setExpanded(true);
        rootNode.setSelectable(allowRootSelection);
        if (allowRootSelection) {
            TreeNode artificialRootNode = new DefaultTreeNode(rootGroup, rootNode);
            artificialRootNode.setExpanded(true);
            if (currentlySelectedGroups != null && currentlySelectedGroups.contains(rootGroup))
                artificialRootNode.setSelected(true);
            artificialRootNode.setSelectable(true);

            queue.add(artificialRootNode);
        } else
            queue.add(rootNode);

        TreeNode currEntry;

        while ((currEntry = queue.poll()) != null) {
            UserGroup group = currEntry.getData() == null ? null : (UserGroup) currEntry.getData();

            if (group != null) {
                for (UserGroup child : group.getChildren()) {
                    if (userGroupToExclude != null && child.equals(userGroupToExclude)) {
                        continue;
                    }

                    TreeNode node = new DefaultTreeNode(child, currEntry);
                    node.setExpanded(true);
                    if (currentlySelectedGroups != null && currentlySelectedGroups.contains(child)) {
                        node.setSelected(true);
                    }
                    if(readOnly)
                        node.setSelectable(false);

                    queue.add(node);
                }
            }
        }
        return rootNode;
    }

    public TreeNode getTree(UserGroup currentlySelectedGroup, UserGroup userGroupToExclude, Boolean allowRootSelection, Boolean readOnly) {
        List<UserGroup> userGroups = null;

        if (currentlySelectedGroup != null) {
            userGroups = new ArrayList<>();
            userGroups.add(currentlySelectedGroup);
        }

        return getMultipleSelectedTree(userGroups, userGroupToExclude, allowRootSelection, readOnly);
    }

    public TreeNode getMultipleSelectedTree(List<UserGroup> currentlySelectedGroups, UserGroup userGroupToExclude, Boolean allowRootSelection, Boolean readOnly) {
        int hashCode = 0;

        hashCode += currentlySelectedGroups == null ? 0 : currentlySelectedGroups.hashCode();
        hashCode += userGroupToExclude == null ? 0 : userGroupToExclude.hashCode();
        hashCode += allowRootSelection.hashCode();
        hashCode += readOnly.hashCode();

        if (cache.containsKey(hashCode)) {
            return cache.get(hashCode);
        } else {
            TreeNode newTree = buildTree(currentlySelectedGroups, userGroupToExclude, allowRootSelection, readOnly);
            cache.put(hashCode, newTree);

            return newTree;
        }
    }

    public void onDragDrop(TreeDragDropEvent event) throws Exception {
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();

        if (dragNode.getData() instanceof UserGroup && dropNode.getData() instanceof UserGroup) {
            ((UserGroup) dragNode.getData()).setParent((UserGroup) dropNode.getData());
            groupService.merge((UserGroup) dragNode.getData());
        }

        invalidateTrees();
    }

    public void invalidateTrees() {
        cache.clear();
    }

}
