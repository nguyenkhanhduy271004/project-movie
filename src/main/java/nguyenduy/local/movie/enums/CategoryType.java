package nguyenduy.local.movie.enums;

import java.util.*;

public enum CategoryType {
  ACTION("Hành động"),
  COMEDY("Hài"),
  DRAMA("Chính kịch"),
  HORROR("Kinh dị"),
  ROMANCE("Lãng mạn"),
  SCI_FI("Khoa học viễn tưởng"),
  DOCUMENTARY("Tài liệu"),
  ANIMATION("Hoạt hình"),
  FANTASY("Giả tưởng"),
  THRILLER("Giật gân");

  private final String name;

  CategoryType(String name) {
    this.name = name;
  }

  public static Map<String, String> type() {
    Map<String, String> typeCodes = new HashMap<>();
    for (CategoryType item : CategoryType.values()) {
      typeCodes.put(item.toString(), item.name);
    }
    return typeCodes;
  }
}
