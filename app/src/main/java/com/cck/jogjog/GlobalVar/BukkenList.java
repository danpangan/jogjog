package com.cck.jogjog.GlobalVar;

import java.util.ArrayList;
import java.util.List;

public class BukkenList {

    public static List<List<String>> shikuchouson = new ArrayList<>();
    public static List<List<String>> jouchomei = new ArrayList<>();

    public static List<List<String>> bukken_list = new ArrayList<>(); // {index, data}

    /*bukken_list blue additional*/
    public static List<List<List<String>>> bukken_list_setsubi = new ArrayList<>(); //{index,{setsubi_index,{name,selected}}}
    public static List<List<String>> bukken_list_imagelist = new ArrayList<>(); //{index,{    image_index,image_mame}}
    public static List<List<Float>> sliderattr = new ArrayList();

    /*Line Line*/
    public static List<List<String>> lineline = new ArrayList<>();

    /*Schools*/
    public static List<List<String>> schools = new ArrayList<>();

    /*not optimized bukken list*/
    public static List<String> bukken_data; //

    public static List<String> tatemono_no = new ArrayList<>();
    public static List<String> bukken_no = new ArrayList<>();
    public static List<String> bukken_name = new ArrayList<>();
    public static List<String> is_disp_bukken_name = new ArrayList<>();
    public static List<String> shubetsu = new ArrayList<>();
    public static List<String> eki1_kanji = new ArrayList<>();
    public static List<String> eki1_distance = new ArrayList<>();
    public static List<String> bus1_kanji = new ArrayList<>();
    public static List<String> bus1_distance = new ArrayList<>();
    public static List<String> eki2_kanji = new ArrayList<>();
    public static List<String> eki2_distance = new ArrayList<>();
    public static List<String> bus2_kanji = new ArrayList<>();
    public static List<String> bus2_distance = new ArrayList<>();
    public static List<String> eki3_kanji = new ArrayList<>();
    public static List<String> eki3_distance = new ArrayList<>();
    public static List<String> bus3_kanji = new ArrayList<>();
    public static List<String> bus3_distance = new ArrayList<>();
    public static List<String> shozaichi = new ArrayList<>();
    public static List<String> zipcode = new ArrayList<>();
    public static List<String> kouzou = new ArrayList<>();
    public static List<String> building_info = new ArrayList<>();
    public static List<String> kanseibi = new ArrayList<>();
    public static List<String> chikunen = new ArrayList<>();
    public static List<String> ido_hokui = new ArrayList<>();
    public static List<String> keido_tokei = new ArrayList<>();
    public static List<String> keido_toukei = new ArrayList<>();
    public static List<String> tenpo_no1 = new ArrayList<>();
    public static List<String> tenpo_no2 = new ArrayList<>();
    public static List<String> tenpo_name = new ArrayList<>();
    public static List<String> tenpo_tel = new ArrayList<>();
    public static List<String> line_id = new ArrayList<>();
    public static List<String> tenpo_name2 = new ArrayList<>();
    public static List<String> tenpo_tel2 = new ArrayList<>();
    public static List<String> gaikan_url = new ArrayList<>();
    public static List<String> bukken_type_name = new ArrayList<>();
    public static List<String> traffic_kanji = new ArrayList<>();

    public static List<List<List<String>>> heya = new ArrayList<>();

    public static void clearbukken(){
        tatemono_no.clear();
        bukken_no.clear();
        bukken_name.clear();
        is_disp_bukken_name.clear();
        shubetsu.clear();
        eki1_kanji.clear();
        eki1_distance.clear();
        bus1_kanji.clear();
        bus1_distance.clear();
        eki2_kanji.clear();
        eki2_distance.clear();
        bus2_kanji.clear();
        bus2_distance.clear();
        eki3_kanji.clear();
        eki3_distance.clear();
        bus3_kanji.clear();
        bus3_distance.clear();
        shozaichi.clear();
        zipcode.clear();
        kouzou.clear();
        building_info.clear();
        kanseibi.clear();
        chikunen.clear();
        ido_hokui.clear();
        keido_tokei.clear();
        tenpo_no1.clear();
        tenpo_no2.clear();
        tenpo_name.clear();
        tenpo_tel.clear();
        line_id.clear();
        tenpo_name2.clear();
        tenpo_tel2.clear();
        gaikan_url.clear();
        bukken_type_name.clear();
        heya.clear();
        traffic_kanji.clear();

    }
}
