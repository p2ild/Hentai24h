package com.p2ild.hentai24h;

/**
 * Created by duypi on 8/28/2016.
 */
public class Hentai {
    private String linkImgPreview;
    private String tenTruyen;
    private String linkTruyen;

    public Hentai(String linkImgPreview, String tenTruyen, String linkTruyen) {
        this.linkImgPreview = linkImgPreview;
        this.tenTruyen = tenTruyen;
        this.linkTruyen = linkTruyen;
    }

    public String getLinkImgPreview() {
        return linkImgPreview;
    }

    public String getTenTruyen() {
        return tenTruyen;
    }

    public String getLinkTruyen() {
        return linkTruyen;
    }
}
