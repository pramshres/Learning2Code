class Celle:  #klasse Celle

    def __init__(self):     #konstruktør med ingen parametere
        self._status = "dod"    #instansvariebelen self._status er definert med stringen "dod" som utgangspunkt

#Endre status

    def settDoed(self):        #metode som setter statusen død
        self._status = "dod"

    def settLevende(self):      #metode som setter statusen levende
        self._status = "levende"

#Hente status

    def erLevende(self):                    #metode som returnerer True hvis cellen er levende
        if self._status == "levende":       #og False hvis cellen er død
            return True
        return False

    def hentStatustegn(self):               #metode som henter statustegn
        if self.erLevende():                #dersom cellen sin status er levende/true returnerer den tegnet "O"
            return "O"
        else:
            return "."                      #ellers returnerer den en punktum
