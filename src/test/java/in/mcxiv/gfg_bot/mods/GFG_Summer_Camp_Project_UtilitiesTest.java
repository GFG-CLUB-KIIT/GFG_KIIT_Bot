package in.mcxiv.gfg_bot.mods;

import in.mcxiv.gfg_bot.utils.Utilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GFG_Summer_Camp_Project_UtilitiesTest {

    @Test
    void testReduceContent() {
        String string = """
                Name: loistoa , Intermediate
                Projects Domain: @Web dev\s
                
                
                Project Name: Historical Monuments\s
                Project Description:
                    Tours and Travel Website.
                    Made using HTML CSS JAVASCRIPT.
                    I want help to Modify it and add frameworks.
                """;
        var spp = Utilities.reduceContent(string);
        assertEquals("Name: loistoa , Intermediate", spp.get(0).s);
        assertEquals("Projects Domain: @Web dev", spp.get(1).s);
        assertEquals("Project Name: Historical Monuments", spp.get(2).s);
        assertEquals("""
                            Project Description:
                            Tours and Travel Website.
                            Made using HTML CSS JAVASCRIPT.
                            I want help to Modify it and add frameworks.""",
                spp.get(3).s);
        var ssp = Utilities.simplifyContent(string);
        assertEquals("Name", ssp.get(0).s);
        assertEquals("loistoa , Intermediate", ssp.get(0).m);
        assertEquals("Projects Domain", ssp.get(1).s);
        assertEquals("@Web dev", ssp.get(1).m);
        assertEquals("Project Name", ssp.get(2).s);
        assertEquals("Historical Monuments", ssp.get(2).m);
        assertEquals("Project Description", ssp.get(3).s);
        assertEquals("""
                        Tours and Travel Website.
                        Made using HTML CSS JAVASCRIPT.
                        I want help to Modify it and add frameworks.""",
                ssp.get(3).m);
    }
}