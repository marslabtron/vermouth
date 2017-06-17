package com.vermouth.db.test;

/**
 * Created by shenhui.ysh on 2017/6/17 0017.
 */
public class CouponsDo {
    private Long id;
    private String coupons_no;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoupons_no() {
        return coupons_no;
    }

    public void setCoupons_no(String coupons_no) {
        this.coupons_no = coupons_no;
    }

    @Override
    public String toString() {
        return "CouponsDo{" +
                "id=" + id +
                ", coupons_no='" + coupons_no + '\'' +
                '}';
    }
}
