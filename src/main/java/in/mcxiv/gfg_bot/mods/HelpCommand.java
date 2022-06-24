package in.mcxiv.gfg_bot.mods;

import in.mcxiv.gfg_bot.GFG_KIIT_Bot;
import in.mcxiv.gfg_bot.Resources;
import in.mcxiv.gfg_bot.SpecialisedListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class HelpCommand extends SpecialisedListenerAdapter {

    private final GFG_KIIT_Bot bot;
    private final EmbedBuilder botDescriptiveEmbed;

    public HelpCommand(GFG_KIIT_Bot bot) {
        this.bot = bot;
        this.botDescriptiveEmbed = new EmbedBuilder()
                .setTitle(Resources.NAME)
                .setDescription(Resources.IS_UNDER_TEST ? "Development Build" : "A simple bot to automate server management.")
                .appendDescription("\nMention mod name to load more details.")
                .appendDescription("\nFor example: `%s help responsiveness helper`".formatted(bot.resources.botCommand))
                .addField("GH Link", "https://github.com/GFG-CLUB-KIIT/GFG_KIIT_Bot/tree/deployment", false)
                .addField("Bot Command", bot.resources.botCommand, false);
        updateBotDescriptiveEmbed();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.bot.jda.upsertCommand("help", "Show some information about this bot.").queue();
    }

    @Override
    public String getName() {
        return "Help Command";
    }

    private void updateBotDescriptiveEmbed() {
        botDescriptiveEmbed.getFields().stream().filter(field -> field.getName().equals("List of all the mods loaded")).findFirst().ifPresent(field -> botDescriptiveEmbed.getFields().remove(field));
        StringBuilder mods = new StringBuilder();
        int length = bot.listenerAdapter.getListeners().stream().map(SpecialisedListenerAdapter::getName).mapToInt(String::length).max().orElse(26);
        bot.listenerAdapter.getListeners()
                .forEach(la -> mods.append(la.isActivated() ? "o:" : "#:").append("  %%-%ds".formatted(length).formatted(la.getName())).append("\n"));
        botDescriptiveEmbed.addField("List of all the mods loaded", "```haskell\n%s\n```".formatted(mods), false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String content = event.getMessage().getContentDisplay();
        if (!content.contains("help")) return;
        content = bot.stripCommand(bot.stripCommand(content), "help").strip();
        if (content.isEmpty())
            sendBotDescriptiveMessage(event.getChannel()::sendMessageEmbeds);
        else
            sendModHelpMessage(event, content);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("help")) return;
        sendBotDescriptiveMessage(event::replyEmbeds);
    }

    private void sendBotDescriptiveMessage(@NotNull Function<MessageEmbed, RestAction<?>> function) {
        updateBotDescriptiveEmbed();
        function.apply(botDescriptiveEmbed.build()).queue();
    }

    private void sendModHelpMessage(MessageReceivedEvent event, String processedContent) {
        if (processedContent.strip().equals("help")) return;
        SpecialisedListenerAdapter la = bot.listenerAdapter.getListenerByName(processedContent);
        if (la == null) {
            event.getMessage().reply("I don't have any mod with that name loaded 🤔...").queue();
            return;
        }
        event.getChannel().sendMessageEmbeds(la.getHelpEmbed()
                .setTitle("Help - %s".formatted(la.getName()))
                .build()).queue();
    }

    @Override
    public EmbedBuilder getHelpEmbed() {
        throw new UnsupportedOperationException("Help command isn't supposed to contain more information.");
    }
}
