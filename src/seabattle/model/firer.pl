/*
X, Y - new position
OldX, OldY - old position

*/

calculate_next_cell(X, Y, -1, -1, _) :-
    random_between(0, 9, X),
    random_between(0, 9, Y).
    

calculate_next_cell(X, Y, OldX, OldY, FireResult) :-
    X is 0, Y is 0.
