package in.mcxiv.gfg_bot.mods;

import in.mcxiv.gfg_bot.GFG_KIIT_Bot;
import in.mcxiv.tryCatchSuite.Try;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GFGSummerCampProjectUtilities extends ListenerAdapter {

    // IMPORTANT, I have not used Category IDs and Channel IDs so that I can test these on another server too.

    private static final String CATEGORY_NAME = "summer project";

    private static final String CHANNEL_NAME = "projects";

    private static final Pattern rgx_INFORMATION_ENTRY = Pattern.compile("^([\\w]+(?: +[\\w]+)*) *: *(.*)$");

    private final GFG_KIIT_Bot bot;

    public GFGSummerCampProjectUtilities(GFG_KIIT_Bot bot) {
        this.bot = bot;
    }

    static ArrayList<SPPair> appendToLastElement(ArrayList<SPPair> list, SPPair two) {
        if (list.size() == 0) return appendAtLast(list, two);
        SPPair pair = list.get(list.size() - 1);
        String one = pair.s.strip();
        two.s = two.s.strip();
        pair.s = "%s\n%s".formatted(one, two.s).strip();
        return list;
    }

    static ArrayList<SPPair> appendAtLast(ArrayList<SPPair> list, SPPair pair) {
        list.add(pair);
        return list;
    }

    static ArrayList<SPPair> mergeLists(ArrayList<SPPair> one, ArrayList<SPPair> two) {
        one.addAll(two);
        return one;
    }

    private static ArrayList<SPPair> reduceSPPairs(ArrayList<SPPair> strings, SPPair pair) {
        return pair.m.find() ? appendAtLast(strings, pair)
                : appendToLastElement(strings, pair);
    }

    static List<SPPair> reduceContent(String content) {
        return content.lines().map(String::strip)
                .map(s -> new SPPair(s, rgx_INFORMATION_ENTRY.matcher(s)))
                .reduce(new ArrayList<>(), GFGSummerCampProjectUtilities::reduceSPPairs, GFGSummerCampProjectUtilities::mergeLists);
    }

    static List<SSPair> simplifyContent(String content) {
        return reduceContent(content)
                .stream().map(spPair -> Try.getAnd(() -> new SSPair(spPair.s)).elseNull())
                .filter(Objects::nonNull)
                .toList();
    }

    private static boolean isOneOf(String actual, String... matches) {
        actual = actual.replaceAll("[^\\w]", "");
        for (String match : matches)
            if (actual.equalsIgnoreCase(match.replaceAll("[^\\w]", "")))
                return true;
        return false;
    }

    private static boolean allIn(String actual, String... matches) {
        actual = actual.replaceAll("[^\\w]", "").toLowerCase();
        for (String match : matches)
            if (!actual.contains(match.replaceAll("[^\\w]", "")))
                return false;
        return true;
    }

    private static boolean isMessageFromSummerCampProjectCategory(Channel channel) {
        if (!(channel instanceof ICategorizableChannel iCategorizableChannel)) return false;
        if (iCategorizableChannel.getParentCategory() == null) return false;
        if (!allIn(iCategorizableChannel.getParentCategory().getName(), CATEGORY_NAME)) return false;
        return true;
    }

    private static boolean isMessageFromSummerCampProjectChannel(Channel channel) {
        if (!isMessageFromSummerCampProjectCategory(channel)) return false;
        if (!channel.getType().isGuild()) return false;
        if (!channel.getName().equals(CHANNEL_NAME)) return false;
        if (channel.getType() != ChannelType.TEXT) return false;
        return true;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!isMessageFromSummerCampProjectChannel(event.getChannel())) return;
        if (isTheMessageAChannelCommand(event)) return;

        var raw_content = bot.stripCommand(event.getMessage().getContentRaw())
                .strip().replaceAll("[\n\r]+", "\n");
        var lines = simplifyContent(raw_content);
        HashMap<String, String> fields = linesDoesntContainAllTheRequiredInformation(lines, event);
        if (fields.isEmpty()) return;

        ChannelAction<TextChannel> channel = event.getGuild().createTextChannel(fields.get("Project's Name"));
        channel.setParent(((TextChannel) event.getChannel()).getParentCategory());
        channel.setTopic(fields.get("Project's Description"));
        channel.queue(textChannel -> sendProjectInfoEmbed(textChannel, fields));
    }

    private boolean isTheMessageAChannelCommand(MessageReceivedEvent event) {
        String contentDisplay = event.getMessage().getContentDisplay();
        if (contentDisplay.contains("\n")) return false;
        if (!contentDisplay.contains("delete")) return false;
        List<GuildChannel> channels = event.getMessage().getMentions().getChannels();
        if (channels.isEmpty()) return false;
        channels = channels.stream().filter(GFGSummerCampProjectUtilities::isMessageFromSummerCampProjectCategory)
                .filter(guildChannel -> !guildChannel.getName().equals(CHANNEL_NAME)).toList();
        if (channels.isEmpty()) return true;
        if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) return true;
        channels.forEach(guildChannel -> guildChannel.delete().queue());
        return true;
    }

    private void sendProjectInfoEmbed(TextChannel textChannel, HashMap<String, String> fields) {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(fields.get("Project's Name"))
                .setColor(Color.BLUE);
        fields.remove("Project's Name");
        fields.forEach((k, v) -> builder.addField(k, v, false));
        textChannel.sendMessageEmbeds(builder.build()).queue();
    }

    private HashMap<String, String> linesDoesntContainAllTheRequiredInformation(List<SSPair> lines, MessageReceivedEvent event) {
        lines = new ArrayList<>(lines);
        ArrayList<String[]> missingFields = new ArrayList<>();
        HashMap<String, String> foundFields = new HashMap<>();

        List<SSPair> dataItem = lines.stream().filter(spPair -> isOneOf(spPair.s, "name")).toList();
        if (dataItem.size() == 1)
            foundFields.put("User Name", dataItem.get(0).m);
        else missingFields.add(new String[]{
                "Author Name",
                """
                    Name: Hello There! How should I address you?
                    How would you describe your programming experience?
                    
                    eg Name: Loistoa, Intermediate
                    """
        });
        lines.removeAll(dataItem);

        dataItem = lines.stream().filter(spPair -> isOneOf(spPair.s, "domain", "project domain", "projects domain")).toList();
        if (dataItem.size() == 1)
            foundFields.put("Project's Domain", dataItem.get(0).m);
        else missingFields.add(new String[]{
                "Project Domain",
                """
                    Domain: Which domain your project comes under?
                    Possible values are @Web Dev, @ML/AI, @CP, @Content, @UI/UX, @Marketing and @Video Editing.
                    Feel free to mention something else.
                    
                    eg Domain: @Web Dev
                    eg Domain: App Development
                    """
        });
        lines.removeAll(dataItem);

        dataItem = lines.stream().filter(spPair -> isOneOf(spPair.s, "project name", "projects name")).toList();
        if (dataItem.size() == 1)
            foundFields.put("Project's Name", dataItem.get(0).m);
        else missingFields.add(new String[]{
                "Project Name",
                """
                    Project Name: What is the name of your project?
                    
                    eg Project Name: Historical Monuments
                    """
        });
        lines.removeAll(dataItem);

        dataItem = lines.stream().filter(spPair -> isOneOf(spPair.s, "description", "project description", "projects description", "desc", "project desc", "projects desc")).toList();
        if (dataItem.size() == 1)
            foundFields.put("Project's Description", dataItem.get(0).m);
        else missingFields.add(new String[]{
                "Project Description",
                """
                    Project Description: What does your project do?
                    
                    eg Project Description:
                        Tours and Travel Website.
                        Made using HTML CSS JAVASCRIPT.
                        I want help in modifying it and add frameworks.
                    """
        });
        lines.removeAll(dataItem);

        lines.forEach(ssPair -> foundFields.put(ssPair.s, ssPair.m));
        if (missingFields.isEmpty() || missingFields.size() == 4) return foundFields;
        foundFields.clear();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Hello there! Welcome to GFG Summer Camp!")
                .setColor(Color.BLUE);
        missingFields.forEach(strings -> embedBuilder.addField(strings[0], "```\n%s\n```\n".formatted(strings[1]), false));

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        return foundFields;
    }

    static final class SPPair {
        public String s;
        public Matcher m;

        SPPair(String s, Matcher m) {
            this.s = s;
            this.m = m;
        }
    }

    static final class SSPair {
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
