package com.yi.psms.model.vo.opinion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ViewDistributionVO {

    @JsonProperty("keywordCount")
    private List<OpinionCountVO> keywordCount;

    @JsonProperty("viewList")
    private List<String> viewList;

}
