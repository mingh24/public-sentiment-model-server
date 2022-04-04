package com.yi.psms.model.vo.opinion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpinionDistributionItem {

    @JsonProperty("attitudeOverallDist")
    private List<OpinionCountItem> attitudeOverallDist;

    @JsonProperty("priceOptionOverallDist")
    private List<OpinionCountItem> priceOptionOverallDist;

    @JsonProperty("lengthOptionOverallDist")
    private List<OpinionCountItem> lengthOptionOverallDist;

}
