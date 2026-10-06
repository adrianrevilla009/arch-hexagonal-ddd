package lab.legacy;

/** The legacy system's model: cryptic names, status codes, amounts as decimal strings. */
public record LegacyOrderRecord(String ordNo, String custCd, String stat, String amtStr, String ccy) {}
