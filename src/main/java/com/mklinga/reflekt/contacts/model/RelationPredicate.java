package com.mklinga.reflekt.contacts.model;

public enum RelationPredicate {
  IS_FRIEND_OF("isFriendOf"),
  IS_MOTHER_OF("isMotherOf"),
  IS_FATHER_OF("isFatherOf"),
  IS_SIBLING_OF("isSiblingOf"),
  IS_COLLEAGUE_OF("isColleagueOf"),
  IS_PARTNER_OF("isPartnerOf"),
  IS_CHILD_OF("isChildOf");

  private final String code;

  RelationPredicate(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
