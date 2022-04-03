package com.yi.psms.model.vo.opinion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpinionDistributionItem {

    @JsonProperty("overallAttitudeDist")
    private List<OpinionCountItem> overallAttitudeDist;

    @JsonProperty("overallPriceOptionDist")
    private List<OpinionCountItem> overallPriceOptionDist;

    @JsonProperty("overallLengthOptionDist")
    private List<OpinionCountItem> overallLengthOptionDist;

}
