package com.example.telegram_bot.dto.superjob.resume;

import lombok.Data;

import java.util.Arrays;
import java.util.stream.Collectors;

@Data
public class Resume {
    private int id;
    private long id_user;
    private String last_profession;
    private int payment;
    private String currency;
    private boolean hide_birthday;
    private int birthday;
    private int birthmonth;
    private int birthyear;
    private int age;
    private Metro[] metro;
    private String address;
    private Citizenship citizenship;
    private Published published;
    private boolean moveable;
    private boolean agreement;
    private boolean is_archive;
    private MoveableTown[] moveable_towns;
    private TypeOfWork type_of_work;
    private PlaceOfWork place_of_work;
    private Education education;
    private MaritalStatus maritalstatus;
    private Children children;
    private BusinessTrip business_trip;
    private Language[][] languages;
    private String[] driving_licence;
    private Catalogue[] catalogues;
    private Town town;
    private Region region;
    private WorkHistory[] work_history;
    private BaseEducationHistory[] base_education_history;
    private EducationHistory[] education_history;
    private String firstname;
    private String middlename;
    private String lastname;
    private String phone1;
    private int timebeg1;
    private int timeend1;
    private String phone2;
    private int timebeg2;
    private int timeend2;
    private String email;
    private String other_contacts;
    private boolean favorite;
    private boolean contacts_bought;
    private String link;
    private String short_link;
    private SocialLink[] social_links;
    private Gender gender;
    private String achievements;
    private String additional_info;
    private long date_published;
    private long date_last_modified;
    private String profession;
    private String recommendations;
    private String photo;
    private PhotoSizes photo_sizes;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("<b>%s</b>", profession) + "\n");
        builder.append(String.format("<b>Город:</b> %s", town.getTitle()) + "\n");
        builder.append(String.format("<b>%s</b>", "Возраст: ") + age + "\n\n");
        builder.append("<b>Тип занятости:</b> " + type_of_work.getTitle() + "\n\n");
        builder.append("<b>Образование:</b> " + education.getTitle() + "\n\n");
        if (languages.length > 0) {
            builder.append("<b>Владение иностранными языками:</b> " +
                    Arrays.stream(languages)
                            .flatMap(Arrays::stream)
                            .map(language -> language.getTitle())
                            .collect(Collectors.joining("\n")) + "\n\n");
        }

        builder.append("<b>Категории и отрасли:</b> \n" +
                Arrays.stream(catalogues)
                        .map(catalogue -> catalogue.getTitle() + " " +
                                Arrays.stream(catalogue.getPositions())
                                        .map(position -> position.getTitle())
                                        .collect(Collectors.joining("\n")))
                        .collect(Collectors.joining("\n")) + "\n\n");
        builder.append(String.format("<b>Состояние резюме - </b>%s", published.getTitle()));

        String result = builder.toString();
        return result;
    }
}
