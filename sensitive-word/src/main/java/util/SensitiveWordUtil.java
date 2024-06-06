package util;

import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 敏感词过滤工具
 * @author gaozijie
 * @since 2024-06-04
 */
@Component
public class SensitiveWordUtil {

    private final static Logger logger = LoggerFactory.getLogger(SensitiveWordUtil.class);

    /** 敏感词文件路径 */
    private final static String SENSITIVE_WORD_FILE_PATH = "./sensitive-word/sensitive.txt";

    /** 无效字符文件路径 */
    private final static String INVALID_CHAR_FILE_PATH = "./sensitive-word/invalid.txt";

    /** 结束标识 */
    private final static String END_TAG = "isEnd";

    /** 匹配规则：最小匹配（敏感词：[你妈，你妈逼]，会优先匹配出'你妈'） */
    public final static int MIN_MATCH_TYPE = 0;
    
    /** 匹配规则：最大匹配（敏感词：[你妈，你妈逼]，会优先匹配出'你妈逼'） */
    public final static int MAX_MATCH_TYPE = 1;

    /**
     * 敏感词字典树
     */
    private static Map<String, Object> sensitiveWordMap;

    /**
     * 无效字符集合
     */
    private static Set<String> invalidCharSet;

    static {
        init();
    }

    /**
     * 过滤敏感词
     * @param str 字符串
     * @return 过滤后的字符串
     */
    public static String filterSensitive(String str) {
        return filterSensitive(str, SensitiveWordUtil.MIN_MATCH_TYPE);
    }

    /**
     * 过滤敏感词
     * @param str 字符串
     * @param matchType 匹配规则           
     * @return 过滤后的字符串
     */
    public static String filterSensitive(String str, int matchType) {
        // 获取文本中的敏感词
        Set<String> sensitiveWords = getSensitiveWords(str, matchType);
        if (CollectionUtils.isEmpty(sensitiveWords)) {
            return str;
        }
        logger.info("字符串：{}，存在以下敏感词：{}，对此进行替换处理...", str, sensitiveWords);
        // replaceAll替换敏感词为'*'
        for (String sensitiveWord : sensitiveWords) {
            str = str.replaceAll(Pattern.quote(sensitiveWord), "*".repeat(sensitiveWord.length()));
        }
        return str;
    }

    /**
     * 检查是否存在敏感词
     * @param str 字符串
     * @return true/false
     */
    public static boolean checkSensitive(String str) {
        if (ObjectUtils.isEmpty(str)) {
            return false;
        }
        int len = getSensitiveWordLen(str, 0, SensitiveWordUtil.MIN_MATCH_TYPE);
        return !Objects.equals(len, 0);
    }

    /**
     * 获取文本中的敏感词
     * @param str 字符串
     * @param matchType 匹配规则           
     * @return 敏感词集合
     */
    private static Set<String> getSensitiveWords(String str, int matchType) {
        Set<String> sensitiveWords = new HashSet<>();
        for (int i=0; i<str.length(); i++) {
            int len = getSensitiveWordLen(str, i, matchType);
            if (Objects.equals(len, 0)) {
                continue;
            }
            sensitiveWords.add(str.substring(i, i+len));
            i = i + len - 1;
        }
        return sensitiveWords;
    }

    /**
     * 匹配，并获取敏感词长度
     * @param str 字符串
     * @param startIndex 开始下标
     * @param matchType 匹配规则                  
     * @return 敏感词长度
     */
    @SuppressWarnings({"unchecked"})
    private static int getSensitiveWordLen(String str, int startIndex, int matchType) {
        // 默认最小匹配规则
        if (!Objects.equals(matchType, SensitiveWordUtil.MIN_MATCH_TYPE) 
                && !Objects.equals(matchType, SensitiveWordUtil.MAX_MATCH_TYPE)) {
            matchType = SensitiveWordUtil.MIN_MATCH_TYPE;
        }
        // 字符匹配
        Map<String, Object> currentMap = sensitiveWordMap;
        int len = 0;
        for (int i=startIndex; i<str.length(); i++) {
            String c = String.valueOf(str.charAt(i));
            // 跳过无效字符（第一个字符是无效字符的话，直接退出）
            if (invalidCharSet.contains(c)) {
                if (Objects.equals(i, startIndex)) {
                    return len;
                }
                continue;
            }
            currentMap = (Map<String, Object>) currentMap.get(c);
            // 没匹配上，返回默认匹配长度
            if (Objects.isNull(currentMap)) {
                return len;
            }
            // 匹配到结束字符，最小匹配直接返回；最大匹配则修改默认匹配长度，继续进行匹配
            if (Objects.equals(currentMap.get(END_TAG), 1)) {
                if (Objects.equals(matchType, MIN_MATCH_TYPE)) {
                    return i - startIndex + 1;
                } else {
                    len = i - startIndex + 1;
                }
            }
        }
        return len;
    }

    /**
     * 初始化
     */
    @SuppressWarnings({"unchecked"})
    private static void init() {
        logger.info("初始化敏感词词库...");
        // 加载无效字符
        List<String> invalidChars = FileUtil.readUtf8Lines(INVALID_CHAR_FILE_PATH);
        invalidCharSet = new HashSet<>(invalidChars) ;
        // 加载敏感词
        sensitiveWordMap = new HashMap<>(16);
        List<String> sensitiveWords = FileUtil.readUtf8Lines(SENSITIVE_WORD_FILE_PATH);
        if (CollectionUtils.isEmpty(sensitiveWords)) {
            return;
        }
        // 构建字典树，"傻逼" = {"傻": {"isEnd": 0, "逼": {"isEnd": 1}}}
        Map<String, Object> currentMap;
        Map<String, Object> nextMap;
        for (String sensitiveWord : sensitiveWords) {
            currentMap = sensitiveWordMap;
            for (char c : sensitiveWord.toCharArray()) {
                nextMap = (Map<String, Object>) currentMap.get(String.valueOf(c));
                if (Objects.isNull(nextMap)) {
                    nextMap = new HashMap<>();
                    nextMap.put(END_TAG, 0);
                    currentMap.put(String.valueOf(c), nextMap);
                }
                currentMap = nextMap;
            }
            currentMap.put(END_TAG, 1);
        }
    }
}
