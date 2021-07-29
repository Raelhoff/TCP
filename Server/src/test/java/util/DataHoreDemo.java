/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
/**
 *
 * @author RaelH
 */
public class DataHoreDemo {
    public static final Locale BRAZIL = new Locale( "pt", "BR" );

    public static void main( String[] args ) throws InterruptedException
    {
        // antes do Java 8
        // basicamente as classes: java.util.Date (e java.sql.Date) desde JDK 1.0
        Date now = new Date(); // o pattern era esse
        System.out.println( now );

        // e para criar uma data precisa, usava-se um tipo Calendar
        Calendar cal = Calendar.getInstance(); // now
        cal.set( 2014, 11, 25 ); // 25/12/2014, pois jan==0
        Date dez25Natal = cal.getTime();
        System.out.println( dez25Natal );
        cal.add( Calendar.DAY_OF_MONTH, 7 ); // +7 dias
        Date umaSemanaDepois = cal.getTime();
        System.out.println( umaSemanaDepois );

        GregorianCalendar gc = new GregorianCalendar();
        gc.set( 1975, 9, 29, 15, 48, 58 );
        gc.add( GregorianCalendar.DATE, 1 );
        Date d2 = gc.getTime();
        System.out.println( d2 );

        DateFormat df = DateFormat.getDateInstance( DateFormat.FULL, BRAZIL );
        System.out.println( df.format( d2 ) );

        // datas eram mutáveis, ou seja
        // código cliente podia cometer falhas, ex:
        // Cliente c = new Cliente();
        // Date d = c.getDataNascimento();
        // d.setTime( 0L ); // resetou a data de nascimento!

        // após o Java 8
        // pacote é o java.time
        // capturar um instante no tempo
        // Instant representa um ponto na linha de tempo
        // com precisão em nanosegundos, ie, alta granularidade
        Instant início =
                Instant.now();// instante corrente em milisegundos deste 1/1/1970
        System.out.println( "Início: " + início );
        Thread.sleep( 2000 );
        Instant fim = Instant.now(); // milisegundos deste 1/jan/1970
        System.out.println( "Fim: " + fim );

        // computando diferenças entre dois Instants
        Duration diff = Duration.between( início, fim );
        System.out.println( "Diferença: " + diff.toMillis() + " milisegundos" );
        // acima, podia usar métodos toNanos, getSeconds, toMinutes, toHours, toDays, etc.

        // manipular partes de datas (mes, ano, dia) sem a granularidade de Instant
        // a precisão é de data, e não precisão de nanosegundo
        // mês é indexado por 1, em vez de 0 como na API Java 7 anterior
        LocalDate localDateNow = LocalDate.now(); // data agora
        LocalDate localDate30out75 = LocalDate.of( 1975, Month.OCTOBER, 30 );
        // note acima que há consistência entre capturar um momento na máquina: usa-se now()
        // ou seja, independente de usar LocalDate, LocalTime, LocalDateTime e Instant
        System.out.println( localDateNow );
        System.out.println( localDate30out75 );

        DayOfWeek dayOfWeek = localDateNow.getDayOfWeek();
        System.out.println( dayOfWeek );
        System.out.println( dayOfWeek.getValue() );

        // Period: computar diferenças entre LocalDate´s, em vez de Instants (c/Duration)
        // until() é bom para encontrar periodo consolidado
        // e ate mesmo as qtds totais: anos, meses dias, usando/puxando CronoUnits entre datas.
        Period p = localDate30out75.until( localDateNow );
        Period p3= Period.between( localDate30out75, localDateNow ); // mesma coisa que anterior
        System.out.println( "Período entre 30/10/75 e  hoje: "
                            + p.getYears() + " ano(s) " +
                            + p.getMonths() + " mes(es) e " +
                            + p.getDays() + " dia(s) " );
        System.out.println( p.getYears() );
        System.out.println( p3.getYears() );

        long anos =
                localDate30out75.until( localDateNow, ChronoUnit.YEARS ); // total de anos
        long meses =
                localDate30out75.until( localDateNow, ChronoUnit.MONTHS ); // total meses: anos * 12
        long dias =
                localDate30out75.until( localDateNow, ChronoUnit.DAYS ); // total de dias: anos * meses * dias
        System.out.println( "anos: " + anos );
        System.out.println( "meses: " + meses );
        System.out.println( "dias: " + dias );

        // método get() pode "puxar" ChronoUnits do período *consolidado*
        // ex: desmembrar o período entre  2010-01-15 to 2011-03-18
        Period p2 =
                Period.between(
                        LocalDate.of( 2010, 1, 15 ),
                        LocalDate.of( 2011, 3, 18 ) );
        int years = p2.getYears(); // ou p.get( ChronoUnit.YEARS )
        long months = p2.get( ChronoUnit.MONTHS ); // cuidado: NÃO é total de meses, i.e
        // (anos*12), mas o que "sobrou" em
        // meses após os anos
        long days = p2.get( ChronoUnit.DAYS ); // o que "sobrou" em dias após meses
        System.out.println( "Período entre 2010-01-15 e  2011-03-18: "
                            + years + " ano(s) " +
                            + months + " mes(es) e " +
                            + days + " dia(s) " );
        System.out.println( "Período entre 2010-01-15 e  2011-03-18: "
                            + p2.getYears() + " ano(s) " +
                            + p2.getMonths() + " mes(es) e " +
                            + p2.getDays() + " dia(s) " ); // mesma coisa que anterior

        // DateAdjuster: útil para adicionar/subtrair quantidade de tempo
        // em Instant e LocalDate
        LocalDate dataPróximoDomingo =
                localDateNow.with( TemporalAdjusters.next( DayOfWeek.SUNDAY ) );
        LocalDate dataDomingoPassado =
                localDateNow.with( TemporalAdjusters.previous( DayOfWeek.SUNDAY ) );
        LocalDate dataPrimeiroDiaMês =
                localDateNow.with( TemporalAdjusters.firstDayOfMonth() );

        // para manipular uma hora do dia
        LocalTime localTimeNow = LocalTime.now(); // agora
        LocalTime localTime155938 =
                LocalTime.of( 15, 59, 38 ); //15:59:38s
        LocalTime localTimeNowPlus8h10min =
                localTimeNow.plusHours( 8 ).plusMinutes( 10 );
        System.out.println( localTimeNow );
        System.out.println( localTime155938 );
        System.out.println( localTimeNowPlus8h10min );

        // para manipular data e hora do dia juntas
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime dateTime30out75at075959 =
                LocalDateTime.of( 1975, 10, 30,
                                  7, 59, 59 );
        System.out.println( localDateTimeNow );
        System.out.println( dateTime30out75at075959 );

        // formatando data e hora
        // DateTimeFormatter possui 3 tipos de formatters:
        //    a) um conjunto pré-definido de formatters padrão,
        //    b) formatters vinculados a um Locale
        //    c) formatters vinculados a um pattern customizado por você

        // a) formatters standard estão disponíveis como constantes: + adequado para a máquina
        DateTimeFormatter isoDate = DateTimeFormatter.ISO_DATE; // formato pré-definido
        System.out.println( isoDate.format( localDateNow ) );
        DateTimeFormatter isoTime = DateTimeFormatter.ISO_TIME; // há várias constantes
        System.out.println( isoTime.format( localTimeNow ) );
        DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME; // outro pré-definido
        System.out.println( isoDateTime.format( localDateTimeNow ) );

        // b) formatters vinculados a um Locale: + adequado aos humanos
        // há 4 tipos, SHORT, MEDIUM, LONG E FULL
        // note o Localized*DATE*
        DateTimeFormatter formatterShort = DateTimeFormatter.ofLocalizedDate( FormatStyle.SHORT );
        DateTimeFormatter formatterMedium = DateTimeFormatter.ofLocalizedDate( FormatStyle.MEDIUM );
        DateTimeFormatter formatterLong = DateTimeFormatter.ofLocalizedDate( FormatStyle.LONG );
        DateTimeFormatter formatterFull = DateTimeFormatter.ofLocalizedDate( FormatStyle.FULL );
        System.out.println( formatterShort.format( localDateNow ) );
        System.out.println( formatterMedium.format( localDateNow ) );
        System.out.println( formatterLong.format( localDateNow ) );
        System.out.println( formatterFull.format( localDateNow ) );

        // os formatters acima usam locale default, mas, para mudar isso, faça:
        DateTimeFormatter formatterShortBR = formatterShort.withLocale( BRAZIL );
        DateTimeFormatter formatterMediumBR = formatterMedium.withLocale( BRAZIL );
        DateTimeFormatter formatterLongBR = formatterLong.withLocale( BRAZIL );
        DateTimeFormatter formatterFullBR = formatterFull.withLocale( BRAZIL );
        System.out.println( formatterShortBR.format( localDateNow ) );
        System.out.println( formatterMediumBR.format( localDateNow ) );
        System.out.println( formatterLongBR.format( localDateNow ) );
        System.out.println( formatterFullBR.format( localDateNow ) );

        // também pode vincular um locale à um dia da semana
        for ( DayOfWeek d :
                DayOfWeek.values() )
        {
            System.out.println(d.getDisplayName( TextStyle.FULL, Locale.ITALIAN ) );
            System.out.println(d.getDisplayName( TextStyle.FULL, BRAZIL) );
        }

        // c) formatters vinculados a um pattern definido por você
        DateTimeFormatter dtfp = DateTimeFormatter.ofPattern( "d/M/yyyy" );
        DateTimeFormatter dtfp2 = DateTimeFormatter.ofPattern( "EEEE, dd/MMMM/yyyy" ).withLocale( BRAZIL );
        System.out.println( dtfp.format( localDateTimeNow ) );
        System.out.println( dtfp2.format( localDateTimeNow ) );

        // montando o próprio formatter
        DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder()
                .appendValue( ChronoField.DAY_OF_WEEK )
                .appendLiteral( "++" )
                .appendValue( ChronoField.DAY_OF_YEAR )
                .appendLiteral( "++" )
                .appendValue( ChronoField.MONTH_OF_YEAR )
                .appendLiteral( "++" )
                .appendValue( ChronoField.DAY_OF_MONTH )
                .appendLiteral( "++" )
                .appendValue( ChronoField.YEAR );
        DateTimeFormatter customizado = dtfb.toFormatter( BRAZIL );
        System.out.println( customizado.format( localDateNow ) );

        // time-zones: https://www.iana.org/time-zones
        System.out.println( ZoneId.getAvailableZoneIds() ); // imprimir zone-ids

        // vincular uma data e hora a um time-zone
        System.out.println(
                ZonedDateTime.of(
                        1500, Month.APRIL.getValue(), 22,
                        7, 59, 59, 0,
                        ZoneId.of( "America/Sao_Paulo" )
                                )
                          ); // 1500-04-22T07:59:59-03:06:28[America/Sao_Paulo]

        // outra forma
        ZonedDateTime zoneDateTime11sepNY =
                ZonedDateTime.of(
                        LocalDate.of( 2011, Month.SEPTEMBER.getValue(), 11 ),
                        LocalTime.of( 10, 45, 59 ),
                        ZoneId.of( "America/New_York" )
                                );
        System.out.println( zoneDateTime11sepNY ); // 2011-09-11T10:45:59-04:00[America/New_York]
        System.out.println( zoneDateTime11sepNY.plus( Period.ofMonths( 2 ) ) );
        System.out.println( zoneDateTime11sepNY.plusHours( 12 ) );
        // converter o mesmo instante no tempo do timezone de NY para São Paulo
        ZonedDateTime zonedDateTimeSP =
                zoneDateTime11sepNY.withZoneSameInstant( ZoneId.of( "America/Sao_Paulo" ) );
        System.out.println( zonedDateTimeSP );
        System.out.println( DateTimeFormatter.RFC_1123_DATE_TIME.format( zoneDateTime11sepNY ) );
        System.out.println( DateTimeFormatter.ISO_DATE_TIME.format( zoneDateTime11sepNY ) );
        System.out.println( DateTimeFormatter.RFC_1123_DATE_TIME.format( zonedDateTimeSP ) );
        System.out.println( DateTimeFormatter.ISO_DATE_TIME.format( zonedDateTimeSP ) );
        System.out.println( DateTimeFormatter.RFC_1123_DATE_TIME.format( zonedDateTimeSP ) );

        // agora buscando e aplicando o time zone de Berlin
        ZonedDateTime zdt = ZonedDateTime.now( ZoneId.of( "Europe/Berlin" ) );
        // definindo um formatador; note o Localized*DATETIME*
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.MEDIUM );
        System.out.println( dateTimeFormatter.format( zdt ) );

        // agora buscando e aplicando o time zone de Recife
        DateTimeFormatter dtfDia = DateTimeFormatter.ofPattern( "d" );
        DateTimeFormatter dtfMes = DateTimeFormatter.ofPattern( "M" );
        DateTimeFormatter dtfAno = DateTimeFormatter.ofPattern( "yy" );
        ZoneId.getAvailableZoneIds()
                .stream()
                .filter( s -> s.contains( "Recife" ) )
                .forEach( s ->
                          {
                              ZonedDateTime zdtREC = ZonedDateTime.now( ZoneId.of( s ) );
                              System.out.println( dateTimeFormatter.format( zdtREC ) );
                          }
                        );
    }
}
