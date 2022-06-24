# GFG KIIT Bot 🤖

![Banner](src/docs/resources/Banner.png)

## 𝐀𝐛𝐨𝐮𝐭 -

Hmm... Someone please help me write a short and simple _about_ in here...

## Current Functionalities

* **Summer Project Camp Helper**<br/>
  A simple automation for creating new channels upon request.<br/>
  [Learn more about this mod here](src/docs/main/SummerCampProjectMod.md)


* **Well, that's it so far.**<br/>
  As new mods are added, this will be updated accordingly.

## How to contribute

* The source root is present at `src/main/java` and the resource root is present at `src/main/resources`.
* The resources must include a resource bundle named `privateResources.properties` with the following keys:
    * `discord_bot_token` - This is the main authentication token used for the deployment bot. One may leave it blank
      and use development mode.
    * `development_discord_bot_token` - This is an alternative token which is used when testing out new features.
    * `discord_bot_invite_link` - This field contains an up-to-date invite link for the bot, which contains only the
      necessary permissions.
* Adding new functionality:
    * Create a new class `MyNewMod` under the package `mods`.
    * Make it extend the class `SpecialisedListenerAdapter`.
    * Optionally override the `isSuperior` method. If it returns `true`, the message events will be called on all
      occasions. If it returns `false`, the events will be called only when the bot is mentioned using the command
      string.
    * Override `getName` to return a suitable name for your mod, for example, `My New Mod`.
    * Override `getHelpEmbed` to return a title-less embed containing information about your mod.
    * Override on of the default JDA events like `onMessageReceived` to start working on your mod.
    * Make sure to read the `Resources` and `Utilities` class.
    * For logging, you can use the generalised logging methods in `GFG_KIIT_Bot`.
    * To register your Mod, navigate to `GFG_KIIT_Bot#initializeBot` add the line: <br/>
      `listenerAdapter.addEventListener(new MyNewMod());`