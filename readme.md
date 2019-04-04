Begin
======
## Motivatie
Ik heb gekozen om mij de komende dagen te verdiepen in Scala. Scala is een programmeer taal die support biedt voor zowel OO als FP (functional programming). Deze taal sprak mij aan omdat hij deze twee technieken combineert. Zo kan ik rustig aan kennis maken met FP in een omgeving waarin ik al bekend ben, het OO paradigma. Dit kennis maken ga ik doen aan de hand van de het boek Seven Languages in Seven Days. Dit boek biedt uitleg over de taal met een aantal opdrachten die je zelf moet maken. Deze opdrachten leiden uiteindelijk tot mijn eindopdracht waarin ik de opgedane kennis ga toepassen. 

Als eindopdracht heb ik besloten om een reddit scraper te maken. Dit programma accepteert een reeks subreddits die hij parallel gaat scrapen om zo te tellen welke woorden hoevaak gebruikt worden. Dit lijkt mij een leuke opdracht omdat ik het idee heb dat subreddits een beetje in hun eigen "bubble" zitten en dat posts vaak hetzelfde taalgebruik hebben.

Ik heb ook een aantal eisen opgesteld voor deze opdracht:

- HTML-pagina's moeten omgezet worden tot een datastructuur zodat je er gemakkelijk zoek queries op uit kan voeren. Je moet html elementen kunnen opzoeken op basis van hun tag, class of id. 
- Dit scrapen en verwerken van de html pagina moet geheel functioneel geprogrammeerd worden. Hier zijn wel een paar uitzonderingen op waar ik later op terug kom (Denk aan een class die de functies bevat om ze gemakkelijk aan te roepen).
- De subreddits moeten parallel verwerkt worden. Omdat het scrapen geheel functioneel geprogrammeerd is leent het zich hier uitstekend voor. 
- De uiteindelijke woord telling moet naar een .JSON bestand geschreven worden om hem zo in te kunnen laden in een programma die er een grafiek van kan maken, of iets in die richting.

Ik vind dat deze opdracht goed bij Scala past omdat Scala goede support biedt voor FP. De subreddits moeten parallel verwerkt worden waar Scala heel sterk in is door zijn immutable variables, een grote regel van het functionele programmeren.

## Installatie
In mijn werk en minor ben ik altijd aan het programmeren in javascript/typescript. Hierdoor ben ik gewend om VS code te gebruiken als mijn IDE, lightweight en oneindig veel extensions beschikbaar. Ik wil daarom ook graag VS code gebruiken voor Scala, maar toen ik de download pagina (https://www.scala-lang.org/download/) opende zag ik dat Intellij goede support biedt voor Scala. Ik wil het liefst zo min mogelijk te doen hebben met installatieproblemen, ben al druk genoeg met het werkelijk programmeren, dus voor deze keer moet ik helaas afscheid nemen van mijn VS code. 
Om IntelliJ te laten werken met Scala moest ik wel eerst de Scala plugin installeren. Dit gedaan, nieuw Scala project aangemaakt en IntelliJ installeerde zelf de Scala SDK. Even snel een hello world programma maken (copy/paste dus) en hij doet het!


Dag 1
======
Dag 1 begon met een rustige introductie. Het eerste wat mij opviel was dat doordat Scala strongly typed is je geen 0/1 kan gebruiken als een boolean. 

```scala
    if(0) {println("Dit is false")}
```

Hier gooit Scala dus een error dat hij een boolean verwacht, maar een integer krijgt. Hetzelfde kan gebeuren denk ik met checks zoals dit
```scala
    if(someVariable) {println("someVariable is gevuld")}
```
Dit kan dus ook niet, het moet een werkelijke vergelijking zijn.
```scala
    if(someVariable == 1) {println("someVariable is gevuld")}
```
Opzich valt dit wel mee, ik denk juist dat het goed is dat dit gebeurd omdat je nu gedwongen wordt om de code consistenter te maken. In tegenstelling tot JavaScrip waar bijna alles wel mag...

## Val vs Var
Scala heeft twee keywords om variabelen te definen: val en var. Waarin je bij Java int of long moet gebruiken is het bij Scala alleen val/var. Scala kan zelf invullen welk datatype gebruikt moet worden. Er is alleen wel een groot verschil tussen val en var. Waar een variable gedefined is met var mutable is, is een variable gedefined met val immutable. Dit haakt ook weer goed in op het FP gedeelte van Scala (https://alvinalexander.com/scala/scala-idiom-immutable-code-functional-programming-immutability). Bij FP moet je er van uit gaan dat alle data immutable is. Hierdoor is het ook beter geschikt voor iets als multi-threading, het is immutable dus geen problemen met concurrency.

## Classes
Hierna behandelde het boek hoe classes werken in Scala. Dit vond ik niet zo interresant omdat het eigenlijk hetzelfde werkt als in Java. Met de uitzondering op traits(interfaces) en auxiliary constructors. Traits in Scala lijken veel op interfaces in Java. 

Auxiliary constructors vind ik lijken op het overloaden van een constructor
```Scala
class Person(first_name: String) { 
  println( "Outer constructor" )
  def this(first_name: String, last_name: String) { 
    this(first_name)
    println( "Inner constructor" )
  } 
  def talk() = println( "Hi" )
}
val bob = new Person( "Bob" ) 
val bobTate = new Person( "Bob" ,  "Tate" )

```
Hierin wordt een class Person aangemaakt. Deze class heeft een constructor met 1 parameter, first_name. Binnen deze constrcutor wordt nog een constructor gemaakt met twee parameters. Aan de hand van de hoeveelheid paramters je meegeeft bij aan het aanmaken van een object wordt de correcte constructor uitgevoerd. Dit lijkt mij erg handig. Ik ben gewend om dit te doen in C# bijvoorbeeld met optionele parameters. Het nadeel hieraan is dat ik dan handmatig moet checken of een paramter is meegegeven, Scala doet dit automatisch voor je.

Inhertance werkt zoals ik verwachte, behalve dat je geen super() hoeft aan te roepen. Dit gebeurt in Scala bij het toewijzen van de super class. 
``` Scala
class Employee(override val name: String, val number: Int) extends Person(name)
```
Hier extends de class Employee de class Person en wordt de paramtere name mee gegeven aan Person, de super class. Rare syntax vind ik het, maar het is maar wat je gewendt ben denk ik dan.

## Tic-Tac-Toe
De oefening van dag 1 was het programeren van een programma die een tic-tac-toe spel kon evalueren, gebruikmakend van classes. Hij moest zeggen of het spel nog niet voorbij was, of het gelijk spel was of wie er gewonnen had. 

Deze opdracht viel mij reuze mee (Source is te vinden in "src/TicTacToe.scala"). Ik ben deze odpracht begonnen met het aanmaken van twee classes, board en line. Board is het speelbord en bestaat uit drie regels (lines). Een line class bestaat bevat een List van drie Strings ("X", "O" of " "). Het opzetten van een spel ziet er als volgt uit:
```Scala
    val line1 = new Line(List("X","O","X"))
    val line2 = new Line(List("X","O","X"))
    val line3 = new Line(List("O","X","O"))
    val board = new Board(List(line1, line2, line3))
```

Nu ik mijn dataset had kon ik beginnen met het schrijven van functies om te checken wat de status van de game is. Dit doe ik door te checken of iedere Line gevuld is, is dit niet het geval dan is het spel nog niet voorbij. Als dit niet het geval is moet ik gaan checken wie er gewonnen heeft. Dit doe ik door de Lines op te delen in rows, columns en diagonals. Hier krijg ik dan 3(rows) + 3(columns) + 2(diagonals) = 8 Lines uit.
```Scala
val rows = lines
  val cols = lines.indices.map(i => {
    val cells = lines.map(row => row.cells(i))
    new Line(List(cells(0), cells(1), cells(2)))
  })

  val diagonals = List( new Line(List(rows(0).cells(0), rows (1).cells(1), rows (2).cells(2))),
                        new Line(List(rows(0).cells(2), rows (1).cells(1), rows (2).cells(0)))  )
```

Ik plak alle Strings uit de lines aan elkaar en check of ze gelijk zijn aan "XXX" of "OOO", als dit zo is dan heeft er iemand gewonnen.

```Scala
def isDominated: String = {
    val x = "XXX"
    val o = "OOO"
    val rowConcatted = cells(0)+cells(1)+cells(2)
    if(rowConcatted == x)
      return "X"
    if(rowConcatted == o)
      return "O"
    return ""
  }
```

Zoals ik al zei vond ik deze opdracht niet spannend/moeilijk. Mijn grootste struggle was hierbij de syntax. Bijvoorbeeld het addresseren van een list/array d.m.v () en geen []. Het returnen van een waarde was ook even wennen. Het keyword ``` return ``` is niet nodig in Scala. Dit vind ik wel raar omdat Scala's syntax vrij streng is en door ``` return ``` weg te laten je het juist weer heel vrij maakt. 

Dag 2
======

Dag twee ging over collections.

## Sets en Maps

Scala heeft Sets en Maps. Deze werken naar mijn idee hetzelfde als in Java. Wat mij hierbij opviel was hoe je de data moet bewerken. In Java zou je iets doen als ```map.add(1, "one") ```. In Scala gebeurt dit met behulp van de operatoren
 ```+ - ++ -- **```  

- Toevoegen van een element ```set + "two" ```
- Verwijderen van een element ```set - "two" ```
- Union / set difference ```set1 ++ set2; set1 -- set2 ```
- Intersect ``` set1 ** set2 ```

De reden hiertoe is denk ik dat je bij FP data en hun gedrag gescheiden zijn. Door functies zoals add toe te voegen aan een set/map overtreedt je deze regel.

## Higher order functions
Nu komen we eindelijk aan bij het functionele programmeren. Collections hebben toegant tot een aantal higher order functions die anonymous functions accepteren als parameter. Ik ben deze syntax erg gewend door JavaScript. Een erg bekende functie is ```map(elem => ...) ```. Deze functie maakt een nieuwe lijst gevuld met wat de anonieme functie returned. Doordat deze een nieuwe lijst maakt (en dus immutable) lijkt mij deze enorm handig bij FP. Ik denk dat ik deze veel ga gebruiken in de eindopdracht. 

Een andere bekende higher order function is de ```foreach(elem => ...)```. Dit is een for each loop implemented op basis van recursie. Dit komt ook weer voort uit een hoofdregel van FP dat je geen loops moet gebruiken maar recursie. De reden hiervoor is dat je in loops altijd data wil bewerken en dat is in strijd met het hele immutable concept. 

## Oefeningen

Foldleft gebruiken en censoring

### FoldLeft
In Javascript werk ik vaak met de ```javascript reduce((acc, cur) => ...)``` functie, de foldleft functie in Scala is precies hetzelfde. De opdracht was om de foldLeft te gebruiken om de lengte van een list van Strings te berekenen

```scala
trait wordCounter {
  val stringList: List[String]
  def getWordCount : Int = {
    stringList.foldLeft(0)((sum, value) => sum + 1)
  }
}
```

Ik heb hier een trait van gemaakt zodat ik hem in de volgende opdracht kon gebruiken. 

```scala
stringList.foldLeft(0)((sum, value) => sum + 1)
```
Hier begin ik met de waarde 0, waar ik iedere keer een 1 aan toe voeg. Hierdoor tel je het aantal elementen, best wel straight forward naar mijn idee.

### Censoring
De opdracht was om een stuk tekst te censoren op basis van woorden die in een bestand staan.

```scala
class Text(val text: String) extends wordCounter with censorable {
  override val stringList: List[String] = ("([\\w]+)".r findAllIn text).toList

  def getCensoredText : String = {
    val censored = censor(stringList)
    return censored;
  }
}
```
De class Text extends de trait wordCounter zodat ik kan tellen hoeveel woorden deze text bevat. Hij heeft ook de trait censorable, later meer hierover. 
De text wordt per woord uit elkaar getrokken met een regex. Deze lijst van strings wordt dan aan de trait censorable gegeven.

```scala 
  def censor(stringList: List[String]): String = {
    val censored = stringList.foldLeft("")((sum, value) => {sum + tryGetValue(wordsToCensor, value) + " "})
    censored
  }
```
Met de foldleft functie bouw ik hier een nieuwe string op. Ik kijk of het huidige woord voorkomt in de censorlijst, als dit zo is plak ik het ge-censorde woord in de string, anders het normale woord.

```scala
  def tryGetValue(words: Map[String, String], str: String): String = {
    if(words.contains(str))
      return words.get(str).mkString("")
    str
  }
```

Het opbouwen van de censor woorden gebeurd op basis van een bestand. Deze lees ik per regel in en split ik op ```:```. Linker gedeelte is de key, rechter gedeelte de value (het "vervangwoord").

```scala
private var wordsToCensor = getCensorship
  private def getCensorship: Map[String, String] = {
    var map = Map(""->"")
    val fileLines = Source.fromFile("censorship.txt").getLines()
    for(line <- fileLines) {
      val pair = line.split(":")
      map += (pair(0) -> pair(1))
    }
    map
  }

```

Bij deze opdracht ben ik wat meer in aanraking gekomen met FP. Zo vond ik bij deze opdracht het gebruik van de foldLeft erg handig. Ook het immutable maken van de text vond ik wel goed omdat je zo niet de originele source van de text kwijt raak. Stel je wilt het censoren optioneel maken (een setting in de applicatie) dan kan je hierdoor gemakkelijk de ge-censorde tekst laten zien of die originele tekst.

Dag 3
======

## Concurrency en Actors

Acties parallel uitvoeren gebeurd in scala met behulp van Actors.
```scala
  def getPageSizeConcurrently() = {
    val caller = self

    for(url <- urls) {
      actor { caller ! (url, PageLoader.getPageSize(url)) }
    }

    for(i <- 1 to urls.size) {
      receive {
        case (url, size) =>
          println("Size for " + url + ": " + size)
      }
    }
  }
```
Dit is een voorbeeld uit het boek. In de eerste for loop roep je een actor aan met message en een functie die uitgevoerd moet worden. In de tweede for loop maak je een receive aan voor iedere url (dus voor ieder request die je in de vorige loop heb aangeroepen). Dit voorbeeld handelt de receive af door middel van pattern matching. Omdat je de request aanroept met als messagetype de variable ```url``` kan je hier naar gaan luisteren in de receive. Zo kan je verschillende acties laten uitvoeren voor verschillende messages die dus asynchroon binnen komen. Dit vind ik lijken op Redux waar je werkt met actions en reducers. Dit vind ik een leuk pattern (kan je het zo noemen?)

## Oefeningen
De oefening van de derde dag was om een tweede message mee te geven aan de actor om de hoeveelheid links op te vragen.
### Tweede message toevoegen
Een tweede actor aanmaken met als message type de string ```"Links"``` leek mij het beste. 
```scala
actor { caller ! ("link", PageLoader.getLinks(url)) }
```

Deze opvangen in de receive:
```scala
case ("link", size) => println("Size for " + size)
```

En het was klaar, heel erg snel. Ik vind dit een heel fijn ontwerp om zo code asynchroon af te laten handelen. Je kan eigenlijk endpoints maken het hun eigen functionaliteit en de Actor handelt zelf af hoe en wat. 

RedditScraper
======

Nu was het dus tijd om te beginnen aan mijn scraper. Op dit moment was ik nog heel erg aan het zoeken naar wat nu wel en wat nu niet toegestaan is bij FP. 
Om het zo barebones mogelijk te houden heb ik besloten om geen gebruik te maken van self-made objects als datastructuur. Als ik deze opdracht OO had uitgeprogrammeerd zou ik een class Element hebben gemaakt die een aantal properties had(id, class inner text). Maar omdat ik dat nu juist niet wilde heb ik ervoor gekozen om alles in een array op te slaan. Deze array bevat voor ieder element een array met de properties van dat element. 

```scala
private def parseHTML(html: String) : Array[Any] = {
    html.indices.map(index => if(html(index) == '<') getNextTag(html, index))
      .filter(_ != (())) //Remove boxedUnits
      .map(element => element.asInstanceOf[Array[Any]])
      .map(element => Array(element(0), element(1), extractProperties(element(0), "class"))) //Add the classes
      .map(element => Array(element(0), element(1), element(2), extractProperties(element(0), "id"))) //Add the id(s)
      .map(element => Array(element(0).asInstanceOf[String].split(" ")(0),element(1),element(2),element(3))) //Remove all the fluff from the selector
      .toArray
  }
```

Dit is de functie die gebruikt word om een html pagina (als string) te transformen naar een datastructuur(array) die ik kan gebruiken om element te zoeken. 

De eerste functie die ik heb gemaakt was de getNextTag functie. Deze functie haalt alle elementen (tags) uit de html pagina. Dit gebeurd door over iedere letter te loopen door middel van de map functie en te controleren of dit het ```<``` karakter was (het begin van een tag). Als dit zo is roep ik de getNextTag functie aan die een element aanmaakt met alle inhoud tussen de ```<...>```

```scala
 private def getNextTag(html: String, index: Int, tag: Array[String] = Array("")): Array[String] = {
    val nextChar = html(index+1)
    if(nextChar == '>') {
      val innerText = getStringUntil(html, index+1, '<')
      Array(tag(0), innerText)
    }
    else {
      getNextTag(html, index+1, Array(tag(0) + nextChar))
    }
  }
```

Dit is de werkelijke getNextTag functie. Hij accepteer de html string, de index van het begin het element in de string en een tag array die hij steeds verder aanvult en uitendelijk returned. Deze functie plakt simpel weg ieder karakter vast aan de al bestaande tag. Zodra hij het ```>``` karakter tegen komt weet hij dat hij nu in het element zit en dat tekst die hierna volgt inner html is. Hij leest alle tekst in tot het begin van een nieuw element en voegt dit toe aan de tag array.

Let op: deze functie leest dus alleen de inner html in tot het begin van een nieuw element. Bij HTML zoals dit
```html
<div>
inner
<p>ik ben een p tag</p>
html
</div>
```
leest deze functie alleen het stukje ```inner``` in als inner html. Echter was deze functionaliteit voldoende voor mijn doeleind.

Een element wat hier deze functie volgt ziet er als volgt uit
```scala
["div": String, "inner html": String]
```

## Ids en Classes
De volgende stap was om de ids en classes van een element te pakken te krijgen. Hiervoor heb ik de volgende functie geschreven:
```scala
  private def extractProperties(element: Any, attr: String): Array[String] = {
    val idsRaw = element.asInstanceOf[String].split(attr +"=\"")
    if(idsRaw.length > 1) idsRaw(1).split("\"")(0).split(" ")
    else Array()
  }
```

Aan deze functie geef je een element mee dus bijvoorbeeld
```html
 div class="title red-background" id="selectable"
```
en een proprety die je wilt extracten, in dit geval "class" of "id". Deze functie haalt dan alle woorden, gesplit op ```" "``` uit de meegeven property. Hij returned deze als een array zodat elementen meerdere classes/ids kunnen hebben.

Op dit punt was ik eindelijk af van de OO denkwijze en begon het FP mij meer te liggen. Tot nu toe returnen alle functies ook een nieuwe variable, immutable dus. 

## Clean up

Omdat  ```getNextTag``` alle tekst tussen de ```<``` en de ```>``` opslaat als tag zodat de extractProperties de properties/attributen kan lezen moet deze uiteindelijk opgeschoond worden. Dit doe ik met een simpele split op ```" "``` zodat het eerste woord gezien wordt als de werkelijke tag.

Na het chainen van deze functies heb ik de html omgezet naar een datastructuur waarin ik kan zoeken naar de elementen die ik wil hebben. Dit is naar mijn weten geheel functioneel gebeurd en het maakt geen gebruik van mutable data. Hierdoor kan het gemakkelijk gebruikt worden bij de parallelen verwerking van de pagina's

## Elementen zoeken

Voor deze toepassing moet ik alle ```<a>``` tags hebben met als class ```title```. Dit doe ik als volgt:

```scala
 parseHTML( loadPage(url))
      .filter(element => hasClass(element.asInstanceOf[Array[Any]], "title"))
      .filter(element => hasSelector(element.asInstanceOf[Array[Any]], "a"))
```

De geparsde HTML filter ik met behulp van de ```hasClass``` en de ```hasSelector``` functie. Nu ik dit zo lees zie ik dat het ```hasTag``` had moeten en zijn en geen ```hasSelector```(ik geef ICSS maar de schuld). Met behulp van deze functies kan je dus een willekeurige pagina "query-en" om zo de elementen op te vragen die je wilt hebben. Nu ik dus toegang had tot de data die ik wilde printen was het tijd om het parallel uit te laten voeren. Dit zou geen probleem moeten zijn omdat alle data immutable is. 

## Parallel

Dit is alle code die nodig was om het parallel te maken
```scala
def getPageSizeConcurrently(): Unit = {
    val caller = self

    for(page <- pages) {
      actor { caller ! (page, RedditPostCounter.printTitleWordCount(page)) }
    }
  }
```

Ik heb hier een for-loop waarin ik voor iedere page (een string naar de locatie van de pagina) een actor aanroep met als message type de page en als functie de ```printTitleWordCount(page)```. Ik hoef in dit geval niets te receiven dus dit is alle code die nodig was. En tot mijn verbazing werkte het ook nog. 

Ik heb gebruik gemaakt van de timer methode uit het boek om zo bij te houden hoe lang de functie uitvoer duurt. Ik heb dit gedaan voor de sequentiele versie en voor de concurrent versie.
```
Method took 2.137646959 seconds.
Method took 0.043542566 seconds.
```
Zo te zien is de parallele uitvoering vele malen sneller, mooi!

Terugblik
======

Ik heb de laatste paar dagen veel leuke nieuwe dingen geleerd. Hiervoor wist ik niet goed wat FP nou precies betekende en was ik "bang" om hier mee te beginnen. Omdat Scala zowel aspecten van OO als FP heeft gaf het mij een vertrouwde omgeving om te experimenteren met FP. 

Tijdens het programmeren van de eindopdracht (wat mij een goede twee dagen heeft gekost) kwam ik steeds meer in de flow van het FP. Zo vind ik de uiteindelijke code er ook heel mooi uitzien

```scala
getRedditPostTitles(url)
      .mkString(" ")
      .split(" ")
      .map(s => removeString(s, "\r"))
      .map(s => removeString(s, "\n"))
      .map(s => removeString(s, "-"))
      .map(s => removeString(s, "/"))
      .filter(s => s != "")
```
Allemaal functies aan elkaar gechained, overzichtelijk en modulair. Ik denk dat ik FP vaker ga toepassen bij toepassingen waar ik een hele reeks bewerkingen moet toepassen op een dataset. Ik ga dan wel gebruik maken van mijn eigen datastructures, dit heb ik nu niet gedaan om mij te focussen op het FP.

Tot nu toe heb ik programma's geschreven die acties asynchroon/parallel uitvoeren in Java en C# (ook Javascript maar dit is niet echt parallel). Daar ging het in mijn geval altijd stroef of ik merkte niet veel verbetering in de snelheid. Toen ik het in Scala deed was het bijna geen werk en leverde het een enorme verbetering in performance op. 

Mijn ervaring met Scala is dus zeer positief en ik wil nu eigenlijk meer leren over het functionele programmeren!