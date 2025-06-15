package com.stctest.anycompmarketplace.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableGenerator {
    
    public static Pageable gen(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable pageElement = PageRequest.of(pageNum - 1, pageSize);
        if (sortBy != null) {
            JSONArray jarr = new JSONArray(sortBy);
            Sort sorted = null;
            for(int a = 0; a < jarr.length(); a++) {
                JSONObject jobj = jarr.getJSONObject(a);
                
                if (a == 0) {
                    if (!StringUtils.isBlank(jobj.optString("colname"))) {
                        sorted = Sort.by(jobj.optString("colname"));
                    }
                    if (jobj.optBoolean("descending") == true) {
                        sorted = sorted.descending();
                    }
                } else {
                    Sort sortedCont = null;
                    if (!StringUtils.isBlank(jobj.optString("colname"))) {
                        sortedCont = Sort.by(jobj.optString("colname"));
                    }
                    if (jobj.optBoolean("descending") == true) {
                        sortedCont = sortedCont.descending();
                    }
                    sorted = sorted.and(sortedCont);
                }
            }
            if (sorted != null) {
                pageElement = PageRequest.of(pageNum - 1, pageSize, sorted);
            }
        }

        return pageElement;
    }

}
