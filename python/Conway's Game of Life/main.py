from spillebrett import Spillebrett #importerer spillebrettet

def main(): #prosedyren main
    print("VELKOMMEN TIL GAME OF LIFE:")
    print()

    rad = int(input("Vennligst skriv inn rader: "))   #brukeren skriver inn rader
    kolonne = int(input("Vennligst skriv inn kolonner: ")) #brukeren skriver inn kolonner

    spillebrett1 = Spillebrett(rad,kolonne) #lager objektet spillebrett1 med brukerens rader og kolonner

    print()
    spillebrett1.tegnBrett() #lager tegnebrettet som generer tilfeldige antall levende celler

    sporsmal = input("Press ENTER for aa fortsette. q + ENTER for aa avslutte: ")  #spørsmål som spør brukeren om å fortsette
    while sporsmal != "q": #så lenge svaret på spørsmålet ikke er q:
        spillebrett1.oppdatering()  #oppdateres spillebrett
        spillebrett1.tegnBrett()    #det oppdaterte spillbrettet tegnes
        if spillebrett1.finnAntallLevende() == 0: #hvis alle cellene er døde
            print("GAME OVER")  #printes game over
            exit() #og programmet stoppes
        sporsmal = input("Press enter for aa fortsette. Skriv inn q og trykk enter for aa avslutte: ")  #looper hvis det fortsatt er levende celler

main()
