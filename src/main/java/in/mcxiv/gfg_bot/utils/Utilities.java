package in.mcxiv.gfg_bot.utils;

import in.mcxiv.tryCatchSuite.Try;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utilities {

    public static final Pattern rgx_INFORMATION_ENTRY = Pattern.compile("^([\\w]+(?: +[\\w]+)*) *: *(.*)$");

    private Utilities() {
    }

    public static boolean areSimilar(String a, String b) {
        return a.replace("[^\\w]", "").toLowerCase().contains(b.toLowerCase());
    }

    public static boolean isOneOf(String actual, String... matches) {
        actual = actual.replaceAll("[^\\w]", "");
        for (String match : matches)
            if (actual.equalsIgnoreCase(match.replaceAll("[^\\w]", "")))
                return true;
        return false;
    }

    public static boolean allIn(String actual, String... matches) {
        actual = actual.replaceAll("[^\\w]", "").toLowerCase();
        for (String match : matches)
            if (!actual.contains(match.replaceAll("[^\\w]", "")))
                return false;
        return true;
    }

    public static ArrayList<SPPair> appendToLastElement(ArrayList<SPPair> list, SPPair two) {
        if (list.size() == 0) return appendAtLast(list, two);
        SPPair pair = list.get(list.size() - 1);
        String one = pair.s.strip();
        two.s = two.s.strip();
        pair.s = "%s\n%s".formatted(one, two.s).strip();
        return list;
    }

    public static ArrayList<SPPair> appendAtLast(ArrayList<SPPair> list, SPPair pair) {
        list.add(pair);
        return list;
    }

    public static ArrayList<SPPair> mergeLists(ArrayList<SPPair> one, ArrayList<SPPair> two) {
        one.addAll(two);
        return one;
    }

    public static ArrayList<SPPair> reduceSPPairs(ArrayList<SPPair> strings, SPPair pair) {
        return pair.m.find() ? appendAtLast(strings, pair)
                : appendToLastElement(strings, pair);
    }

    public static List<SPPair> reduceContent(String content) {
        return content.lines().map(String::strip)
                .map(s -> new SPPair(s, rgx_INFORMATION_ENTRY.matcher(s)))
                .reduce(new ArrayList<>(), Utilities::reduceSPPairs, Utilities::mergeLists);
    }

    public static List<SSPair> simplifyContent(String content) {
        return reduceContent(content)
                .stream().map(spPair -> Try.getAnd(() -> new SSPair(spPair.s)).elseNull())
                .filter(Objects::nonNull)
                .toList();
    }

    public static final class SPPair {
        public String s;
        public Matcher m;

        public SPPair(String s, Matcher m) {
            this.s = s;
            this.m = m;
        }
    }

    public static final class SSPair {
        public String s;
        public String m;

        public SSPair(String pair) {
            this(pair.substring(0, pair.indexOf(":")).strip(), pair.substring(pair.indexOf(":") + 1).strip());
        }

        SSPair(String s, String m) {
            this.s = s;
            this.m = m;
        }
    }
}
