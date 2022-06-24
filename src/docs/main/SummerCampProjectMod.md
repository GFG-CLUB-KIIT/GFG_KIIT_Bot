# Summer Project Camp Mod

package: `mods.GFGSummerProjectCampUtilities`

![Banner](../resources/SummerCampProjectMod/Summer%20Project%20Camp%20Banner.png)

---

This is a simple mod which creates a new channel for a developer participating in GFG Summer Project Camp.

The workings are simple, one can give their project details in `#projects` channel in the following format:

```
Name: Anirudh Sharma
Domain: App Development
Project Name: GFG KIIT Bot

Project Description: 
A simple discord bot written in Java for
automating server management for various
events organised in the GFG KIIT discord
server.

Github Link: https://github.com/GFG-CLUB-KIIT/GFG_KIIT_Bot/tree/deployment 
```

This will create a new channel as

![New Channel Creation](../resources/SummerCampProjectMod/New%20Channel%20Creation.png)

---

The mod has a few basic accessibility features, which help users provide all the required information.
If their message lack one of the required fields, then the bot will suggest adding them.

![New Channel Creation](../resources/SummerCampProjectMod/Information%20Deficiency.png)

---

The mod also has a built-in `delete` command, in case the mods decide to get rid of certain channels.

Only a user with `manage channel` permissions will be able to use it.

The syntax is simple, just type `delete` and `#` mention all the channels to be deleted.

![Delete Command](../resources/SummerCampProjectMod/Delete%20Channels.png)

---

There are a lot of restriction built in the mod, to prevent the bot acting in unwanted places.
It includes checking for channel names and the category where the channel belongs.

In general, it simply asserts that the category name should have the words `summer` and `project` 
and the channel name should only contain the word `projects`. 

---

This is a superior mod, which means, one doesn't need to provide the bot prefix.