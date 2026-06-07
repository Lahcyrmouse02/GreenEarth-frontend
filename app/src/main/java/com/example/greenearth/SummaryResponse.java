package com.example.greenearth;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SummaryResponse {
    private String status;
    private SummaryData data;

    public String getStatus() { return status; }
    public SummaryData getData() { return data; }

    public class SummaryData {
        @SerializedName("todays_footprints")
        private float todaysFootprints;

        @SerializedName("carbon_saved")
        private float carbonSaved;

        // TAMBAHAN BARU: Menangkap array breakdown dari Node.js
        @SerializedName("breakdown")
        private List<BreakdownItem> breakdownList;

        public float getTodaysFootprints() { return todaysFootprints; }
        public float getCarbonSaved() { return carbonSaved; }
        public List<BreakdownItem> getBreakdownList() { return breakdownList; }
    }
}