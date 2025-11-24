package com.urlshortener.service.impl;

import com.urlshortener.service.UserAgentService;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserAgentServiceImpl implements UserAgentService {

    private final UserAgentAnalyzer userAgentAnalyzer;

    public UserAgentServiceImpl() {
        this.userAgentAnalyzer = UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withCache(10000)
                .build();
    }

    @Override
    public Map<String, String> parseUserAgent(String userAgentString) {
        UserAgent agent = userAgentAnalyzer.parse(userAgentString);
        Map<String, String> result = new HashMap<>();

        result.put("deviceType", agent.getValue(UserAgent.DEVICE_CLASS));
        result.put("browser", agent.getValue(UserAgent.AGENT_NAME));
        result.put("operatingSystem", agent.getValue(UserAgent.OPERATING_SYSTEM_NAME));

        return result;
    }
}
