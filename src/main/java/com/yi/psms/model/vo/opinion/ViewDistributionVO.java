package com.yi.psms.model.vo.opinion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ViewDistributionVO {

    @JsonProperty("viewList")
    private List<String> viewList;

}
