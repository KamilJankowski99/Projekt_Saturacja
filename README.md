# Projekt_Saturacja
Program pozawala na edycje obrazka "jpg" i umożliwia zmiany kanałów barw RGB (Red, Green, Blue) oraz HSL (Hue, Saturation, Lightness).
Do zmiany parametrów obrazu obliczenia wykonywane są za pomocą frameworka OpenCL.
W tym celu zostały napisane dwa programy "kernel" Open CL - jeden dla zmian RGB drugi dla HSL.
Każda instancja kernela OpenCL jest uruchamiana jako osobny wątek, nie ma tu potrzeby aby te wątki synchronizować gdyż obliczenia dla każdego piksela są wykonywane niezależnie od obliczeń dla pozostałych pikseli (do przeliczenia położenia koloru w przestrzeni barw dla każdego piksela jest uruchamiany osobny wątek OpenCL).
Zmiana czerwieni, zieleni i błekitu odbywa się poprzez dodanie wybranej sliderem (javafx) wartości niemalże wprost, natomiast dla barwy, saturacji i jasności jest to bardziej złożone, gdyż najpierw następuje przeliczenie położenia w przestrzeni barw RGB na położenie w przestrzeni barw HSL, następnie dodawane są wybrane sliderem wartości i następuje kolejna konwersja HSL do RGB.


#Kierunki dalszego rozwoju aplikacji
Największym zaobserwowanym obecnie problemem jest utrata danych kiedy kolor pikseli "wychodzi" poza zakres określony dla przestrzeni barw. Wówczas taki piksel ma kolor "graniczny" ale powrót do wyjściowych różnic między pikselami nie jest już możliwy (np. piksel1 ma kanal R = 240 a piksel2 R=255, po podbiciu czerwieni o 15 oba piksele maja kanal R=255 i zmniejszenie czerwieni o 15 skutkuje dla obu pikseli R = 240). Można to zjawisko w prosty sposób wyeliminować dodając głębszy bufor koloru i rzutować go na właściwą przestrzeń barw RGB. Jednakże uzyskane efekty kolorystyczne spodobały mi się na tyle, że tego zaniechałem, gdyż nie jest to sedno zadania, a wygląda ciekawie.

Kolejnym tematem jest minimalne zniekształcenie koloru przy przekształceniu RGB-HSL-RGB prawdopodobnie wynikające z niedokładności algorytmu lub problemu z zaokrągleniami. Skutkuje to sporadycznie powstawaniem widocznych artefaktów na obrazku nawet podczas pracy w dopuszczalnych dla przestrzeni barw zakresach.
