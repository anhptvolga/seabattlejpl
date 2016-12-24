/*
X, Y - new position
OldX, OldY - old position

*/

inSea(X, Y) :-
    0 =< X, X < 10,
    0 =< Y, Y < 10.

nextPos(X, Y, Nx, Ny) :-
    (
        Y == 9, X < 9, 
        Nx is X + 1, Ny is 0
    );
    (
        Y < 9,
        Nx is X, Ny is Y + 1
    ).

isNotFired(X, Y, FireResult) :-
    fired(X, Y, FireResult, Res),
    Res == 0.


fired(X, Y, FireResult, Res) :-
    nth0(X, FireResult, Ys, _),
    nth0(Y, Ys, Res, _).

get_possible_point(Res, FireResult, X, Y) :-
    write(X), write(" "), write(Y), nl,
    (
        (
            isNotFired(X, Y, FireResult),
            Temp = [[X, Y]]
        );  Temp = []
    ),
    (
        (
            nextPos(X, Y, Nx, Ny),
            get_possible_point(Temp2, FireResult, Nx, Ny)
        ); true
    ),
    append(Temp, Temp2, Res).

calculate_next_cell(X, Y, -1, -1, FireResult) :-
    get_possible_point(PosPoint, FireResult, 0, 0),
    length(PosPoint, Len),
    write(Len), write("--"),
    random_between(1, Len, Pos),
    write(Pos), nl,
    nth1(Pos, PosPoint, [X, Y], _).

calculate_next_cell(X, Y, OldX, OldY, FireResult) :-
    X is 0, Y is 0.


point(0, 0).
point(0, 1).
point(0, 2).
point(0, 3).
point(0, 4).
point(0, 5).
point(0, 6).
point(0, 7).
point(0, 8).
point(0, 9).
point(1, 0).
point(1, 1).
point(1, 2).
point(1, 3).
point(1, 4).
point(1, 5).
point(1, 6).
point(1, 7).
point(1, 8).
point(1, 9).
point(2, 0).
point(2, 1).
point(2, 2).
point(2, 3).
point(2, 4).
point(2, 5).
point(2, 6).
point(2, 7).
point(2, 8).
point(2, 9).
point(3, 0).
point(3, 1).
point(3, 2).
point(3, 3).
point(3, 4).
point(3, 5).
point(3, 6).
point(3, 7).
point(3, 8).
point(3, 9).
point(4, 0).
point(4, 1).
point(4, 2).
point(4, 3).
point(4, 4).
point(4, 5).
point(4, 6).
point(4, 7).
point(4, 8).
point(4, 9).
point(5, 0).
point(5, 1).
point(5, 2).
point(5, 3).
point(5, 4).
point(5, 5).
point(5, 6).
point(5, 7).
point(5, 8).
point(5, 9).
point(6, 0).
point(6, 1).
point(6, 2).
point(6, 3).
point(6, 4).
point(6, 5).
point(6, 6).
point(6, 7).
point(6, 8).
point(6, 9).
point(7, 0).
point(7, 1).
point(7, 2).
point(7, 3).
point(7, 4).
point(7, 5).
point(7, 6).
point(7, 7).
point(7, 8).
point(7, 9).
point(8, 0).
point(8, 1).
point(8, 2).
point(8, 3).
point(8, 4).
point(8, 5).
point(8, 6).
point(8, 7).
point(8, 8).
point(8, 9).
point(9, 0).
point(9, 1).
point(9, 2).
point(9, 3).
point(9, 4).
point(9, 5).
point(9, 6).
point(9, 7).
point(9, 8).
point(9, 9).
point(-1, -1).
