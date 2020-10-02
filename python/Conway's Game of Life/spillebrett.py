from random import randint  #importerer randint
from celle import Celle     #impoterer klassen Celle

class Spillebrett: #klasse spillebrett

    def __init__(self, rader, kolonner):  #konstruktør som tar imot to dimensjoner (rader og kolonner)
        self._rader = rader        #definerer instansvariabler
        self._kolonner = kolonner
        self._rutenett = []        #instansvariebelen self._rutenett er en tom liste
        self._generasjonnr = 0     #instansvariebelen self._generasjonnr startes på 0

        for x in range(self._rader):  #for x antall rader brukeren skriver,
            nyListe = []               #lager den x antall lister (nyListe). altså hvis bruken skriver 5 lager den 5 nyLister
            for x in range(self._kolonner): #for x antall kolonner brukeren skriver,
                celle = Celle()  #plasseres variabelen celle som er celle objekter
                nyListe.append(celle)       #i disse nyListene. Altså hvis brukeren skriver 4 kolonner, plasseres 4 celler i hver av de nyListene laget tidligere
            self._rutenett.append(nyListe)  #hver av de nyListene blir til slutt lagt i en ny liste kalt self._rutenett, så vi får en nøstet liste
        self.generer()                      #kaller på metoden generer som begynner på linje 31

    def tegnBrett(self):           #metoden som tegner spillebrettet

        for i in self._rutenett:   #for løkke som går først inne i nyListene
            for x in i:            #for løkke som går deretter inne i hvert element i nyListene
                print(x.hentStatustegn(), end=" ") #for hvert element i nyLista hentes deres statustegn (akkurat nå er alle cellene døde)
                                    #for å unngå linjeskifte etter hvert utskrift, avsluttes koden med en tom string (end = " ")
            print()                 #skriver print() her for at hver av disse nyListene skal printes på en egen linje hver, sånn at ikke alle printes på den samme linjen
        print("Generasjon:", self._generasjonnr,"- Antall levende celler:",self.finnAntallLevende())
        #Etter å ha printet tegnebrettet printes generasjonsnr og antall levende celler ^


    def generer(self):            #metode som generer et tilfeldige levende celer
        for i in self._rutenett:    #for løkke som går først inne i nyListene
            for x in i:             #for løkke som går deretter inne i hvert element i nyListene
                rand = randint(0,2) #variabelen rand generer tall mellom 0 og 2 (altså 0,1,2) for hvert element i nyListene
                if rand == 2:       #hvis tallet vi får er en 2
                    x.settLevende() #settes elementet/cellen levende, hvis tallet er 0 og 1 settes den død ("."), altså false
#Kunne også ha gjort det på denne måten:
    #def generer(self):
        #for i in range(self._rader):
            #for j in range(self._kolonner):
                #tall = randint(0,2)
                #if tall == 0:
                    #self._rutenett[i][j].settLevende()

    def finnNabo(self,y,x): #rad, kolonne #Metode som finner naboene til hvert enkelt celle (maks: 8). Koden fikk vi fra universitetet
        naboliste = []  #lager en tom liste for å legge til alle naboene til cellen
        for naboRad in range(y-1, y+2):
            for naboKol in range(x-1, x+2):
                gyldig = True
                if naboRad == y and naboKol == x:
                    gyldig = False  #ugyldig hvis den sentrale cellen telles med
                if naboRad < 0 or naboRad >= self._rader:
                    gyldig = False  #ugyldig hvis indeksen er mindre enn 0 eller større enn radene
                if naboKol < 0 or naboKol >= self._kolonner:
                    gyldig = False #ugyldig hvis indeksen er mindre enn 0 eller større enn kolonnene
                if gyldig:
                    naboliste.append(self._rutenett[naboRad][naboKol]) #setter alle naboene til cellen i en liste, (her naboliste)
        return naboliste

    def oppdatering(self):  #Metoden oppdatering endrer statusen på cellen ved hjelp av statusen til nabocellene
        deadcell = []       #deadcell (liste som konverterer levende celler til død)
        alivecell = []      #alivecell (liste som konverterer døde celler til levende)

        for i in range(self._rader):    #for løkke som går først inne i radene
            for x in range(self._kolonner): #for løkke som går gjennom hvert element i radene
                celle_status = self._rutenett[i][x].erLevende() #sjekker celle status
                naboliste = self.finnNabo(i,x)  #lager en naboliste for hver celle
                levendetall = 0                 #variabelen levendetall settes 0 på starten, økes med antall naboceller, cellen har rundt seg.
                for nabo in naboliste:          #for hver nabo i nabolisten:
                    nabo_status = nabo.erLevende()  #variabelen nabo_status sjekker om naboen er levende
                    if nabo_status == True:         #hvis naboen er levende
                        levendetall += 1            #økes levendetall med 1 (så hvis 3 av naboene er levende, blir levendetall 3)

                if celle_status == True:         #Hvis cellen er levende,
                    if levendetall < 2 or levendetall > 3:   #men har naboceller som er mindre enn 2 og større enn 3
                        deadcell.append(self._rutenett[i][x])   #settes cellen i listen deadcell

                if celle_status == False:        #Hvis cellen er død
                    if levendetall == 3:           #men den har 3 naboer rundt seg som er levende
                        alivecell.append(self._rutenett[i][x])  #settes den døde cellen i liste alivecell

        for x in deadcell:  #for alle levende celler inne i lista deadcell
            x.settDoed()    #settes de død

        for x in alivecell: #for alle døde celler inne i lista alivecell
            x.settLevende() #settes de levende

        self._generasjonnr += 1 #hver gang metoden oppdateringen kjøres øker instansvariabelen self._generasjonnr med 1

    def finnAntallLevende(self): #metode som finner antall levende celler i spillebrettet
        teller = 0    #variabelen teller er 0 på starten

        for i in self._rutenett:    #for løkke som går gjennom self._rutenett, altså nylistene inne i self.rutenett
            for celle in i:             #for løkke som går gjennom hvert element i nylistene
                if celle.erLevende() == True: #for hver celle som er levende, altså True,
                    teller += 1       #økes variabelen teller med 1
        return teller    #returnerer teller
