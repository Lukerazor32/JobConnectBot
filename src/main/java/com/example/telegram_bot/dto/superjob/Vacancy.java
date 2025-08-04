package com.example.telegram_bot.dto.superjob;

import com.example.telegram_bot.dto.superjob.resume.Catalogue;
import com.example.telegram_bot.dto.superjob.resume.Language;
import com.example.telegram_bot.dto.superjob.resume.Town;
import lombok.Getter;

import java.util.List;

@Getter
public class Vacancy implements Comparable<Vacancy> {
    private boolean canEdit;
    private boolean is_closed;
    private int id;
    private int id_client;
    private int payment_from;
    private int payment_to;
    private int date_pub_to;
    private int date_archived;
    private int date_published;
    private String address;
    private String profession;
    private String work;
    private String compensation;
    private String candidat;
    private List<Metro> metro;
    private String currency;
    private String vacancyRichText;
    private CovidVaccinationRequirement covid_vaccination_requirement;
    private String external_url;
    private String contact;
    private boolean moveable;
    private boolean agreement;
    private boolean anonymous;
    private boolean is_archive;
    private boolean is_storage;
    private TypeOfWork type_of_work;
    private PlaceOfWork place_of_work;
    private Education education;
    private Experience experience;
    private MaritalStatus maritalStatus;
    private Children children;
    private Client client;
    private Language[][] languages;
    private Agency agency;
    private Town town;
    private boolean already_sent_on_vacancy;
    private boolean rejected;
    private List<Object> response_info;
    private String phone;
    private List<Object> phones;
    private String fax;
    private List<Object> faxes;
    private boolean favorite;
    private String client_logo;
    private boolean highlight;
    private int age_from;
    private int age_to;
    private String firm_name;
    private String firm_activity;
    private String firm_link;
    private int vacancy_count;
    private String staff_count;
    private String clientLogoUrl;
    private boolean shortReg;
    private boolean isBlocked;
    private boolean isBlacklisted;
    private int registeredDate;
    private List<Object> driving_licence;
    private List<Catalogue> catalogues;
    private float latitude;
    private float longitude;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("<b>%s</b>", profession) + "\n");
        if (!town.getTitle().equals("")) {
            builder.append(String.format("<b>Город:</b> %s", town.getTitle()) + "\n");
        }
        if (address != null && !address.equals("")) {
            builder.append(String.format("<b>Адрес:</b> %s", address) + "\n");
        }
        builder.append("\n");

        builder.append(String.format("<b>Описание:</b>\n%s", candidat) + "\n\n");

        return builder.toString();
    }

    @Override
    public int compareTo(Vacancy vacancy) {
        return Integer.compare(vacancy.date_published, this.date_published);
    }
}
