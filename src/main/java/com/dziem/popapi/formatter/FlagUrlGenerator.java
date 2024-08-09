package com.dziem.popapi.formatter;

public class FlagUrlGenerator {
    public static void main(String[] args) {
        String data = "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('China 1989', 'CN1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('India 1989', 'IN1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Soviet Union 1989', 'SU1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('United States 1989', 'US1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Indonesia 1989', 'ID1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Brazil 1989', 'BR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Japan 1989', 'JP1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Bangladesh 1989', 'BD1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Nigeria 1989', 'NG1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Pakistan 1989', 'PK1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Mexico 1989', 'MX1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Germany 1989', 'DE1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Vietnam 1989', 'VN1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Philippines 1989', 'PH1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Thailand 1989', 'TH1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Turkey 1989', 'TR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('United Kingdom 1989', 'UK1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('France 1989', 'FR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Italy 1989', 'IT1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Egypt 1989', 'EG1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('South Korea 1989', 'KR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Myanmar 1989', 'MM1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Iran 1989', 'IR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Spain 1989', 'ES1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Argentina 1989', 'AR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Colombia 1989', 'CO1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Canada 1989', 'CA1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Morocco 1989', 'MA1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Tanzania 1989', 'TZ1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Sudan 1989', 'SD1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Algeria 1989', 'DZ1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Poland 1989', 'PL1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Uganda 1989', 'UG1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Kenya 1989', 'KE1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Ghana 1989', 'GH1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Saudi Arabia 1989', 'SA1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Afghanistan 1989', 'AF1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Chile 1989', 'CL1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Cambodia 1989', 'KH1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Netherlands 1989', 'NL1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Romania 1989', 'RO1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Greece 1989', 'GR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('North Korea 1989', 'KP1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Czech Republic 1989', 'CZ1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Portugal 1989', 'PT1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Madagascar 1989', 'MG1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Australia 1989', 'AU1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('New Zealand 1989', 'NZ1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Zaire 1989', 'ZR1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Peru 1989', 'PE1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Bolivia 1989', 'BO1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Venezuela 1989', 'VE1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Paraguay 1989', 'PY1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Cuba 1989', 'CU1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Malaysia 1989', 'MY1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Honduras 1989', 'HN1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Haiti 1989', 'HT1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Laos 1989', 'LA1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('El Salvador 1989', 'SV1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Dominican Republic 1989', 'DO1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Hungary 1989', 'HU1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Nicaragua 1989', 'NI1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Yemen 1989', 'YE1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Zimbabwe 1989', 'ZW1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Senegal 1989', 'SN1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Somalia 1989', 'SO1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Sri Lanka 1989', 'LK1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Nepal 1989', 'NP1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Ecuador 1989', 'EC1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Cameroon 1989', 'CM1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Mozambique 1989', 'MZ1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Angola 1989', 'AO1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Jordan 1989', 'JO1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Uruguay 1989', 'UY1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Mongolia 1989', 'MN1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Zambia 1989', 'ZM1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Guatemala 1989', 'GT1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Lebanon 1989', 'LB1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Guinea 1989', 'GN1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Guinea-Bissau 1989', 'GW1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Namibia 1989', 'NA1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Botswana 1989', 'BW1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Gabon 1989', 'GA1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Luxembourg 1989', 'LU1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Iceland 1989', 'IS1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Malta 1989', 'MT1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Belize 1989', 'BZ1989');\n" +
                "INSERT INTO Country (COUNTRY_NAME, GENC) VALUES ('Seychelles 1989', 'SC1989');";
        String[] split = data.split(";");
        String lines = "";
        String genc;
        for(int i = 0; i < split.length ;i++) {
            genc = split[i].substring(split[i].length()-8,split[i].length()-2);
            lines += split[i].substring(0,40) + ",FLAG_URL" + split[i].substring(40,split[i].length()-2)
                    + "', '/images/" + genc + ".png'" + split[i].substring(split[i].length()-1) + ";";
        }
        System.out.println(lines);
    }
}
