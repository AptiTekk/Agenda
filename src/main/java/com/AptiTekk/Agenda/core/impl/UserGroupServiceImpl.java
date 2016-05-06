package com.AptiTekk.Agenda.core.impl;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import com.AptiTekk.Agenda.core.UserGroupService;
import com.AptiTekk.Agenda.core.entity.QUserGroup;
import com.AptiTekk.Agenda.core.entity.UserGroup;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class UserGroupServiceImpl extends EntityServiceAbstract<UserGroup>
    implements UserGroupService {

  QUserGroup userGroupTable = QUserGroup.userGroup;

  public UserGroupServiceImpl() {
    super(UserGroup.class);
  }

  @PostConstruct
  public void init() {
    loadTree();
  }

  @Override
  public void loadTree() {
    entityManager.createQuery("SELECT g from UserGroup g left join fetch g.children").getResultList(); 
  }

  @Override
  public UserGroup findByName(String userGroupName) {
    return new JPAQuery(entityManager).from(userGroupTable)
        .where(userGroupTable.name.eq(userGroupName)).singleResult(userGroupTable);
  }
}
