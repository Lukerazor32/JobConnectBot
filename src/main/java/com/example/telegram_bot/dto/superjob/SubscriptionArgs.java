package com.example.telegram_bot.dto.superjob;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Builder
@Getter
public class SubscriptionArgs {
    private final int resumeId;
    private final int[] town;
    private final List<Integer> catalogues;

    public Map populateQueries() {
        Map queries = new HashMap<>();
        if(nonNull(resumeId)) {
            queries.put("id_resume", resumeId);
        }
        if(nonNull(town)) {
            queries.put("t", town);
        }
        if(nonNull(catalogues)) {
            queries.put("catalogues", catalogues);
        }
        return queries;
    }
}
