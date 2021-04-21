package com.cck.jogjog.GlobalVar;

import android.graphics.Bitmap;


import com.cck.jogjog.Fragments.blue.address.blueAddressDistrictSelectionFragment;
import com.cck.jogjog.Fragments.blue.address.blueAddressPropertyDetailFragment;
import com.cck.jogjog.Fragments.blue.address.blueAddressRentListFragment;
import com.cck.jogjog.Fragments.blue.line.blueLinePropertyDetailFragment;
import com.cck.jogjog.Fragments.blue.line.blueLineRentListFragment;
import com.cck.jogjog.Fragments.blue.line.blueLineStationSelectionFragment;
import com.cck.jogjog.Fragments.blue.location.blueLocationPropertyDetailFragment;
import com.cck.jogjog.Fragments.blue.school.blueSchoolPropertyDetailFragment;
import com.cck.jogjog.Fragments.blue.school.blueSchoolRentListFragment;
import com.cck.jogjog.Fragments.green.address.greenAddressDistrictSelectionFragment;
import com.cck.jogjog.Fragments.green.address.greenAddressRentListFragment;
import com.cck.jogjog.Fragments.green.line.greenLinePropertyDetailFragment;
import com.cck.jogjog.Fragments.green.line.greenLineRentListFragment;
import com.cck.jogjog.Fragments.green.line.greenLineStationSelectionFragment;
import com.cck.jogjog.Fragments.green.location.greenLocationPropertyDetailFragment;
import com.cck.jogjog.Fragments.green.school.greenSchoolPropertyDetailFragment;
import com.cck.jogjog.Fragments.green.school.greenSchoolRentListFragment;
import com.cck.jogjog.Fragments.yellow.address.yellowAddressDistrictSelectionFragment;
import com.cck.jogjog.Fragments.yellow.address.yellowAddressPropertyDetailFragment;
import com.cck.jogjog.Fragments.yellow.address.yellowAddressRentListFragment;
import com.cck.jogjog.Fragments.yellow.line.yellowLinePropertyDetailFragment;
import com.cck.jogjog.Fragments.yellow.line.yellowLineRentListFragment;
import com.cck.jogjog.Fragments.yellow.line.yellowLineStationSelectionFragment;
import com.cck.jogjog.Fragments.yellow.location.yellowLocationPropertyDetailFragment;
import com.cck.jogjog.Fragments.yellow.school.yellowSchoolPropertyDetailFragment;
import com.cck.jogjog.Fragments.yellow.school.yellowSchoolRentListFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.fragment.app.Fragment;

public class GlobalVar {

    //Line
    public static List<String> selectedline;
    public static Boolean isapplylinesearchsetting = false;

    //LocationSwapping
    public static String previousScreen = "";
    public static int selectedtab;
    //Municipality
    private static List<String> code_jis = new ArrayList<>();
    private static String municipality_name = "";

    //Districts
    private static List<String> code_chouaza = new ArrayList<>();
    private static List<String> allcode_chouaza = new ArrayList<>();

    public static List<List<String>> data_jouchoumelist = new ArrayList<>();
    public static int propertycount = 0;

    //Rooms
    private static List<String> addheya_no = new ArrayList<>();
    private static String detailed_bukken_no = "";
    private static Bitmap bitmap_panorama;
    private static String movie_files = "";
    //Web Area
    public static String msg = "";
    private static String code_route;
    private static String code_traffic;
    private static String code_traffic_company;
    private static String traffic_kanji;

    //Code Stations
    private static List<String> code_station = new ArrayList<>();
    private static List<String> allcode_station = new ArrayList<>();
    public static List<List<String>> data_codestation = new ArrayList<>();

    //School

    //Webtown
    private static String web_town;
    private static String school_name;
    private static String web_town_name;
    /*****************************************
     *              Variables                 *
     *****************************************/
    //Prefecture Code
    private static String pref_code = "";
    private static List<String> web_area = new ArrayList<>();
    public static List<List<String>> data_webarea = new ArrayList<>();
    private static List<String> allweb_area = new ArrayList<>();
    public static List<List<String>> data_schooltype = new ArrayList<>();

    //School Type
    private static List<String> school_type = new ArrayList<>();
    private static List<String> allschool_type = new ArrayList<>();

    //Map Address
    private static String bukken_name;
    private static String eki1_kanji;
    private static String bukken_type_name;
    private static String building_info;
    private static String shozaichi;
    private static String gaikan_url;
    private static String chikunen;
    private static String latitude;
    private static String longitude;
    public static String lat = "0", lat2 = "0", lon = "0", lon2 = "0";

    //Map Heya
    private static String chinryo_heya;
    private static String madori_heya;
    private static String heya_kaisu;
    private static String bukken_no;
    private static String kanseibi_heya;
    private static String heya_no;
    private static String tatemono_no;
    private static String tenpo_no;
    private static String map_bukken_url;

    //Search settings
    private static Fragment parent_fragment;
    private static String search_url;

    //Map to List
    public static Boolean isFromMap;
    public static String urlFromMap;

    //Search Settings

    //type 4
    public static String chinryo1_search;
    public static String chinryo2_search;
    public static String menseki1_search;
    public static String menseki2_search;

    //Favorites
    public static String favoritesSrc;
    public static String contact_bukken_no;
    public static Boolean isAddedToFavorites = false;
    /****************************************
     *               Methods                 *
     ****************************************/

    public static String gettraffic_kanji() {
        return traffic_kanji;
    }

    public static void settraffic_kanji(String traffickanji) {
        traffic_kanji = traffickanji;
    }

    /////////////////////////////////////////////////////////////////////////////////
    public static List<String> getallcode_station() {
        return allcode_station;
    }

    public static void addcode_station(String codestation) {
        code_station.add(codestation);
    }

    public static void removecode_station(String codestation) {
        code_station.remove(codestation);
    }

    public static void addallcode_station(String codestation) {
        allcode_station.add(codestation);
    }

    /////////////////////////////////////////////////////////////////////////////////
    public static List<String> getallweb_area() {
        return allweb_area;
    }

    public static void addweb_area(String webarea) {
        web_area.add(webarea);
    }

    public static List<String> getselectedweb_area() {
        return web_area;
    }

    public static void removeweb_area(String webarea) {
        web_area.remove(webarea);
    }

    public static void addallweb_area(String webarea) {
        allweb_area.add(webarea);
    }

    /////////////////////////////////////////////////////////////////////////////////
    public static List<String> getAllschool_type() {
        return allschool_type;
    }

    public static void addschool_type(String schooltype) {
        school_type.add(schooltype);
    }

    public static void removeschool_type(String schooltype) {
        school_type.remove(schooltype);
    }

    public static void addallschool_type(String schooltype) {
        allschool_type.add(schooltype);
    }

    /////////////////////////////////////////////////////////////////////////////////
    public static String getcode_route() {
        return code_route;
    }

    public static void setcode_route(String coderoute) {
        code_route = coderoute;
    }

    public static String getcode_traffic() {
        return code_traffic;
    }

    public static void setcode_traffic(String codetraffic) {
        code_traffic = codetraffic;
    }

    public static String getcode_traffic_company() {
        return code_traffic_company;
    }

    public static void setcode_traffic_company(String codetraffic_company) {
        code_traffic_company = codetraffic_company;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getpref_code() {
        return pref_code;
    }

    public static void setpref_code(String prefcode) {
        pref_code = prefcode;
    }

    public static void addcode_jis(String codejis) {
        code_jis.add(codejis);
    }

    public static void removecode_jis(String codejis) {
        code_jis.remove(codejis);
    }

    public static void clearcodestation() {
        code_station.clear();
        allcode_station.clear();
    }

    public static void clearwebarea() {
        web_area.clear();
        allweb_area.clear();
    }

    public static void clearschooltype() {
        school_type.clear();
        allschool_type.clear();
    }

    public static List<String> getselectedcode_station() {
        return code_station;
    }

    public static String getcode_station() {
        String codestation = "";
        int firstflg = 0;

        Iterator<String> iterator = code_station.iterator();
        while (iterator.hasNext()) {
            if (firstflg == 0) {
                codestation = iterator.next();
                firstflg = 1;
            } else {
                codestation = codestation + "," + iterator.next();
            }
        }
        //}
        return codestation;
    }

    public static String getweb_area() {
        String webarea = "";
        int firstflg = 0;

        Iterator<String> iterator = web_area.iterator();
        while (iterator.hasNext()) {
            if (firstflg == 0) {
                webarea = iterator.next();
                firstflg = 1;
            } else {
                webarea = webarea + "," + iterator.next();
            }
        }
        //}
        return webarea;
    }

    public static String getschool_type() {
        String schooltype = "";
        int firstflg = 0;

        Iterator<String> iterator = school_type.iterator();
        while (iterator.hasNext()) {
            if (firstflg == 0) {
                schooltype = iterator.next();
                firstflg = 1;
            } else {
                schooltype = schooltype + "," + iterator.next();
            }
        }
        //}
        return schooltype;
    }

    public static String getcode_jis() {
        String codejis = "";
        int firstflg = 0;
        // = code_jis.get(0);
        //if (code_jis.size()>1) {
        Iterator<String> iterator = code_jis.iterator();
        while (iterator.hasNext()) {
            if (firstflg == 0) {
                codejis = iterator.next();
                firstflg = 1;
            } else {
                codejis = codejis + "," + iterator.next();
            }
        }
        //}
        return codejis;
    }

    public static List<String> getselectedcode_jis() {
        return code_jis;
    }


    public static String getlowestcode_jis() {
        String lowestcodejis;
        lowestcodejis = "";

        if (code_jis.size() > 0) {

            lowestcodejis = code_jis.get(0);

            Iterator<String> iterator = code_jis.iterator();
            while (iterator.hasNext()) {
                if (Integer.parseInt(lowestcodejis) > Integer.parseInt(iterator.next())) {
                    lowestcodejis = iterator.next();
                }
            }
        }
        return lowestcodejis;
    }

    public static void setmunicipality_name(String municipalityname) {
        municipality_name = municipalityname;
    }

    public static String getmunicipality_name() {
        return municipality_name;
    }

    public static void addcode_chouaza(String codechouza) {
        code_chouaza.add(codechouza);
    }

    public static void addallcode_chouaza(String codechouza) {
        allcode_chouaza.add(codechouza);
    }

    public static void removecode_chouaza(String codechouza) {
        code_chouaza.remove(codechouza);
    }

    public static String getcode_chouaza() {
        String codechouza = "";
        int firstflg = 0;
        //codechouza=code_chouaza.get(0);

        //if(code_chouaza.size()>1) {
        Iterator<String> iterator = code_chouaza.iterator();
        while (iterator.hasNext()) {
            if (firstflg == 0) {
                codechouza = iterator.next();
                firstflg = 1;
            } else {
                codechouza = codechouza + "," + iterator.next();
            }
        }
        //}
        return codechouza;
    }

    public static List<String> getselectedcode_chouaza() {
        return code_chouaza;
    }

    public static int getlowestcode_chouaza() {
        int lowestcodechouza;
        lowestcodechouza = 0;

        if (code_chouaza.size() != 0) {

            lowestcodechouza = Integer.parseInt(code_chouaza.get(0));

            Iterator<String> iterator = code_chouaza.iterator();
            while (iterator.hasNext()) {
                if (lowestcodechouza > Integer.parseInt(iterator.next())) {
                    lowestcodechouza = Integer.parseInt(iterator.next());
                }
            }
        }
        return lowestcodechouza;
    }

    public static List<String> getallcode_chouaza() {
        return allcode_chouaza;
    }

    public static void clearcodes() {
        pref_code = "";
        code_jis.clear();
        municipality_name = "";
        code_chouaza.clear();
        allcode_chouaza.clear();
        addheya_no.clear();
        search_url = "";
    }

    public static void clearchouazacode() {
        code_chouaza.clear();
        allcode_chouaza.clear();
    }

    public static void clearcodejis() {
        code_jis.clear();
        municipality_name = "";
    }
    public static void clearpreviousscreen(){
        previousScreen = "";
    }
    public static void clearheya_no() {
        addheya_no.clear();
    }

    public static void addheya_no(String heyano) {
        addheya_no.add(heyano);
    }

    public static void removeheya_no(String heyano) {
        addheya_no.remove(heyano);
    }

    //town select
    public static String getweb_town() {
        return web_town;
    }

    public static void setweb_town(String webtown) {
        web_town = webtown;
    }

    public static String getweb_town_name() {
        return web_town_name;
    }

    public static void setweb_town_name(String webtownname) {
        web_town_name = webtownname;
    }

    //school names
    public static String getschool_name() {
        return school_name;
    }

    public static void setschool_name(String schoolname) {
        school_name = schoolname;
    }


    // Map Address

    public static String getMap_bukken_url() {
        return map_bukken_url;
    }

    public static void setMap_bukken_url(String mapbukkenurl) {
        map_bukken_url = mapbukkenurl;
    }

    public static String getBukken_name() {
        return bukken_name;
    }

    public static void setBukken_name(String bukkenName) {
        bukken_name = bukkenName;
    }


    public static String getshozaichi() {
        return shozaichi;
    }

    public static void setshozaichi(String _shozaichi) {
        shozaichi = _shozaichi;
    }

    public static String getgaikan_url() {
        return gaikan_url;
    }

    public static void setgaikan_url(String gaikanurl) {
        gaikan_url = gaikanurl;
    }

    public static String getchikunen() {
        return chikunen;
    }

    public static void setchikunen(String _chikunen) {
        chikunen = _chikunen;
    }

    public static String geteki1_kanji() {
        return eki1_kanji;
    }

    public static void seteki1_kanji(String eki1kanji) {
        eki1_kanji = eki1kanji;
    }

    public static String getbukken_type_name() {
        return bukken_type_name;
    }

    public static void setbukken_type_name(String bukkentypename) {
        bukken_type_name = bukkentypename;
    }

    public static String getbuilding_info() {
        return building_info;
    }

    public static void setbuilding_info(String buildinginfo) {
        building_info = buildinginfo;
    }

    public static String getkanseibi() {
        return building_info;
    }

    //Heya
    public static String getKanseibi() {
        return kanseibi_heya;
    }

    public static void setKanseibi(String kanseibi) {
        kanseibi_heya = kanseibi;
    }

    public static String getchinryo_heya() {
        return chinryo_heya;
    }

    public static void setchinryo_heya(String chinryo) {
        chinryo_heya = chinryo;
    }

    public static String getmadori_heya() {
        return madori_heya;
    }

    public static void setmadori_heya(String madori) {
        madori_heya = madori;
    }

    public static String getheya_kaisu() {
        return heya_kaisu;
    }

    public static void setheya_kaisu(String heyakaisu) {
        heya_kaisu = heyakaisu;
    }

    public static String getbukken_no() {
        return bukken_no;
    }

    public static void setbukken_no(String bukkenno) {
        bukken_no = bukkenno;
    }

    public static String getheya_no() {
        return heya_no;
    }

    public static void setheya_no(String heyano) {
        heya_no = heyano;
    }

    public static String gettatemono_no() {
        return tatemono_no;
    }

    public static void settatemono_no(String tatemonono) {
        tatemono_no = tatemonono;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String lat) {
        latitude = lat;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String lon) {
        longitude = lon;
    }

    public static String getdetailed_bukken_no() {
        return detailed_bukken_no;
    }

    public static void setdetailed_bukken_no(String detailedbukkenno) {
        detailed_bukken_no = detailedbukkenno;
    }

    public static Bitmap getpanoramabitmap() {
        return bitmap_panorama;
    }

    public static void setpanoramabitmap(Bitmap panoramabmp) {
        bitmap_panorama = panoramabmp;
    }

    public static String getmovie_files() {
        return movie_files;
    }

    public static void setmovie_files(String moviefiles) {
        movie_files = moviefiles;
    }

    public static Fragment getparent_fragment() {
        return parent_fragment;
    }

    public static void setparent_fragment(String fragmentName) {

        switch (fragmentName) {

            //search settings
            //yellow
            case "yellowAddressDistrictSelectionFragment":
                parent_fragment = new yellowAddressDistrictSelectionFragment();
                break;
            case "yellowAddressRentListFragment":
                parent_fragment = new yellowAddressRentListFragment();
                break;
            case "yellowLineStationSelectionFragment":
                parent_fragment = new yellowLineStationSelectionFragment();
                break;
            case "yellowLineRentListFragment":
                parent_fragment = new yellowLineRentListFragment();
                break;
            case "yellowSchoolRentListFragment":
                parent_fragment = new yellowSchoolRentListFragment();
                break;
            //blue
            case "blueAddressDistrictSelectionFragment":
                parent_fragment = new blueAddressDistrictSelectionFragment();
                break;
            case "blueAddressRentListFragment":
                parent_fragment = new blueAddressRentListFragment();
                break;
            case "blueLineStationSelectionFragment":
                parent_fragment = new blueLineStationSelectionFragment();
                break;
            case "blueLineRentListFragment":
                parent_fragment = new blueLineRentListFragment();
                break;
            case "blueSchoolRentListFragment":
                parent_fragment = new blueSchoolRentListFragment();
                break;

            //green
            case "greenAddressDistrictSelectionFragment":
                parent_fragment = new greenAddressDistrictSelectionFragment();
                break;
            case "greenAddressRentListFragment":
                parent_fragment = new greenAddressRentListFragment();
                break;
            case "greenLineStationSelectionFragment":
                parent_fragment = new greenLineStationSelectionFragment();
                break;
            case "greenLineRentListFragment":
                parent_fragment = new greenLineRentListFragment();
                break;
            case "greenSchoolRentListFragment":
                parent_fragment = new greenSchoolRentListFragment();
                break;

            //contact form
            //yellow
            case "yellowAddressPropertyDetailFragment":
                parent_fragment = new yellowAddressPropertyDetailFragment();
                break;
            case "yellowLocationPropertyDetailFragment":
                parent_fragment = new yellowLocationPropertyDetailFragment();
                break;
            case "yellowLinePropertyDetailFragment":
                parent_fragment = new yellowLinePropertyDetailFragment();
                break;
            case "yellowSchoolPropertyDetailFragment":
                parent_fragment = new yellowSchoolPropertyDetailFragment();
                break;
            //blue
            case "blueAddressPropertyDetailFragment":
                parent_fragment = new blueAddressPropertyDetailFragment();
                break;
            case "blueLocationPropertyDetailFragment":
                parent_fragment = new blueLocationPropertyDetailFragment();
                break;
            case "blueLinePropertyDetailFragment":
                parent_fragment = new blueLinePropertyDetailFragment();
                break;
            case "blueSchoolPropertyDetailFragment":
                parent_fragment = new blueSchoolPropertyDetailFragment();
                break;

            //green
            case "greenAddressPropertyDetailFragment":
                //parent_fragment = new greenAddressPropertyDetailFragment();
                break;
            case "greenLocationPropertyDetailFragment":
                parent_fragment = new greenLocationPropertyDetailFragment();
                break;
            case "greenLinePropertyDetailFragment":
                parent_fragment = new greenLinePropertyDetailFragment();
                break;
            case "greenSchoolPropertyDetailFragment":
                parent_fragment = new greenSchoolPropertyDetailFragment();
                break;
            default:
                break;
        }
    }

    public static String getsearch_url() {
        return search_url;
    }

    public static void setsearch_url(String searchurl) {
        search_url = searchurl;
    }


    public static String getTenpo_no() {
        return tenpo_no;
    }

    public static void setTenpo_no(String tenpono) {
        tenpo_no = tenpono;
    }

    /************************************************
     *            SEARCH SETTINGS
     ***********************************************/


    /*** TYPE 4 ****/

    public static String getchinryo1_search() {
        return chinryo1_search;
    }

    public static void setchinryo1_search(String chinryo1Search) {
        chinryo1_search = chinryo1Search;
    }

    public static String getchinryo2_search() {
        return chinryo2_search;
    }

    public static void setchinryo2_search(String chinryo2Search) {
        chinryo2_search = chinryo2Search;
    }

    public static String getmenseki1_search() {
        return menseki1_search;
    }

    public static void setmenseki1_search(String menseki1Search) {
        menseki1_search = menseki1Search;
    }

    public static String getmenseki2_search() {
        return menseki2_search;
    }

    public static void setmenseki2_search(String menseki2Search) {
        menseki2_search = menseki2Search;
    }

    /*Favorites*/
    public static String getFavoritesSrc() {
        return favoritesSrc;
    }

    public static void setFavoritesSrc(String favoritesSrc) {
        GlobalVar.favoritesSrc = favoritesSrc;
    }

    public static String getContact_bukken_no() {
        return contact_bukken_no;
    }

    public static void setContact_bukken_no(String contact_bukken_no) {
        GlobalVar.contact_bukken_no = contact_bukken_no;
    }
}
