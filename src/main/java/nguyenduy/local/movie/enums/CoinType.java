package nguyenduy.local.movie.enums;

import java.util.HashMap;
import java.util.Map;

public enum CoinType {
  COIN_100(100),
  COIN_200(200),
  COIN_500(500),
  COIN_1000(1000);

  private final int value;

  CoinType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static Map<String, Integer> getCoinTypes() {
    Map<String, Integer> coinTypes = new HashMap<>();
    for (CoinType coin : CoinType.values()) {
      coinTypes.put(coin.name(), coin.getValue());
    }
    return coinTypes;
  }
}