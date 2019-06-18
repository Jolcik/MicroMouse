# MicroMouse
Kreator i symulator do labiryntów dla robotów MicroMouse

Od razu chciałbym powiedzieć, że napisany tutaj kod jest tragiczny. To znaczy nie ma tutaj żadnej większej logiki za strukturą kodu, co wynika z faktu że kiedy to pisałem nie znałem nawet pojęcia wzorca projektowego, ani zasad czystego kodu. Nie ma żadnej architektury oraz oddzielenia logiki od warstwy technicznej (graficznej). Patrząc po czasie naprawdę stwierdzam, że kod jest tragiczny i proszę się nim nie sugerować. Swoje błędy poprawiam w innych projektach (np. projekt Tetrisa na Androida, gdzie kod jest napisany o wiele lepiej, według zasad SOLID oraz wzorca projektowego MVVM).

Program jest prostym narzędziem pozwalającym na graficzne zbudowanie (lub wygenerowanie, a potem poprawienie) labiryntu do robota Micro Mouse. Można także obejrzeć ikonę robota, który na podstawie stworzonych rozkazów porusza się po labiryncie. Zaznaczam że sam program nie generuje tras tylko jest swoistym "buforem" w całym procesie. Tworzy się w nim labirynt, a potem sprawdza wynik działania swojego programu. Dokładna instrukcja znajduje się w pdfie w spakowanym zipie "ZbudowanyProgram" gdzie jest także już zbudowany plik jar z programem. Można także ją znaleźć w głównym folderze repozytorium pt. "Poradniczek.pdf".

Mimo tragicznego kodu program pisało się bardzo miło i mam nadzieję, że komuś w przyszłości pomoże.

PK
