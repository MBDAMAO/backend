package com.damao.constant;

public class AuditConstant {
    /**
     * 参考腾讯云COS审核结果Result字段
     */
    public static final String PASSED = "0";
    public static final String NOT_PASSED = "1";
    public static final String REQUIRE_HUMAN = "2";

    /**
     *  数据库字段
     */
    public static final Integer INIT_PASS_STATUS = -1;

    public static final Integer ON_JUDGING = 0;
    public static final Integer JUDGE_OVER = 1;
    public static final Integer JUDGE_ERROR = 2;

}
