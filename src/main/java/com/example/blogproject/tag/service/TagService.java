package com.example.blogproject.tag.service;

import com.example.blogproject.tag.entity.Tag;
import com.example.blogproject.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

    // 블로그의 전체 태그 조회 및 사용 횟수 계산
    public Map<String, Long> getTagCountsByBlogId(Long blogId, String currentUsername) {
        List<Object[]> tagCounts = tagRepository.findTagsWithPostCountByBlogId(blogId, currentUsername);
        Map<String, Long> tagCountMap = new LinkedHashMap<>();
        long totalCount = 0;

        for (Object[] result : tagCounts) {
            Tag tag = (Tag) result[0];
            Long count = (Long) result[1];
            if (count > 0) {
                tagCountMap.put(tag.getName(), count);
                totalCount += count;
            }
        }

        tagCountMap = Stream.concat(
                Stream.of(Map.entry("전체 보기", totalCount)),
                tagCountMap.entrySet().stream()
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return tagCountMap;
    }
}
