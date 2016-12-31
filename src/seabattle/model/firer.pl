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

check_around(X, Y, FireResult) :-
    X1 is X - 1, X2 is X + 1,
    Y1 is Y -1, Y2 is Y + 1,
    ( (inSea(X, Y1),fired(X, Y1, FireResult, R), R < 1); true),
    ( (inSea(X, Y2), fired(X, Y2, FireResult, R), R < 1); true),
    ( (inSea(X, Y),fired(X, Y, FireResult, R), R < 1); true),
    ( (inSea(X1, Y),fired(X1, Y, FireResult, R), R < 1); true),
    ( (inSea(X1, Y1),fired(X1, Y1, FireResult, R), R < 1); true),
    ( (inSea(X1, Y2),fired(X1, Y2, FireResult, R), R < 1); true),
    ( (inSea(X2, Y),fired(X2, Y, FireResult, R), R < 1); true),
    ( (inSea(X2, Y1),fired(X2, Y1, FireResult, R), R < 1); true),
    ( (inSea(X2, Y2),fired(X2, Y2, FireResult, R), R < 1); true).

get_possible_point(Res, FireResult, X, Y) :-
    /* write(X), write(" "), write(Y), nl, */
    (
        (
            isNotFired(X, Y, FireResult),
            check_around(X, Y, FireResult),
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

/******************************************/
calc_vertical_up(X, Y, Nx, Ny, _, 1, 0) :- Nx is X, Ny is Y.
calc_vertical_up(X, Y, Nx, Ny, FireResult, 1, 1) :- calc_vertical_up(X, Y, Nx, Ny, FireResult).
calc_vertical_up(_, _, _, _, _, 1, -1) :- fail.
calc_vertical_up(X, Y, Nx, Ny, FireResult) :-
    Xx is X - 1,
    inSea(Xx, Y),
    fired(X, Y, FireResult, R1), fired(Xx, Y, FireResult, R2),
    calc_vertical_up(Xx, Y, Nx, Ny, FireResult, R1, R2).

calc_vertical_down(X, Y, Nx, Ny, _, 1, 0) :- Nx is X, Ny is Y.
calc_vertical_down(X, Y, Nx, Ny, FireResult, 1, 1) :- calc_vertical_down(X, Y, Nx, Ny, FireResult).
calc_vertical_down(_, _, _, _, _, 1, -1) :- fail.
calc_vertical_down(X, Y, Nx, Ny, FireResult) :-
    Xx is X + 1,
    inSea(Xx, Y),
    fired(X, Y, FireResult, R1), fired(Xx, Y, FireResult, R2),
    calc_vertical_down(Xx, Y, Nx, Ny, FireResult, R1, R2).

calc_vertical(X, Y, Nx, Ny, FireResult) :-
    calc_vertical_up(X, Y, Nx, Ny, FireResult);
    calc_vertical_down(X, Y, Nx, Ny, FireResult).

/******************************************/
calc_horizon_left(X, Y, Nx, Ny, _, 1, 0) :- Nx is X, Ny is Y.
calc_horizon_left(X, Y, Nx, Ny, FireResult, 1, 1) :- calc_horizon_left(X, Y, Nx, Ny, FireResult).
calc_horizon_left(X, Y, Nx, Ny, FireResult) :-
    Yy is Y - 1,
    inSea(X, Yy),
    fired(X, Y, FireResult, R1), fired(X, Yy, FireResult, R2),
    calc_horizon_left(X, Yy, Nx, Ny, FireResult, R1, R2).

calc_horizon_right(X, Y, Nx, Ny, _, 1, 0) :- Nx is X, Ny is Y.
calc_horizon_right(X, Y, Nx, Ny, FireResult, 1, 1) :- calc_horizon_right(X, Y, Nx, Ny, FireResult).
calc_horizon_right(X, Y, Nx, Ny, FireResult) :-
    Yy is Y + 1,
    inSea(X, Yy),
    fired(X, Y, FireResult, R1), fired(X, Yy, FireResult, R2),
    calc_horizon_right(X, Yy, Nx, Ny, FireResult, R1, R2).

calc_horizon(X, Y, Nx, Ny, FireResult) :-
    calc_horizon_left(X, Y, Nx, Ny, FireResult);
    calc_horizon_right(X, Y, Nx, Ny, FireResult).


/******************************************************/

is_vert(X, Y, FireResult) :-
    (X1 is X + 1, inSea(X1, Y), fired(X1, Y, FireResult, R1), R1 > 0);
    (X2 is X - 1, inSea(X2, Y), fired(X2, Y, FireResult, R2), R2 > 0).
is_hor(X, Y, FireResult) :-
    (Y1 is Y + 1, inSea(X, Y1), fired(X, Y1, FireResult, R1), R1 > 0);
    (Y2 is Y - 1, inSea(X, Y2), fired(X, Y2, FireResult, R2), R2 > 0).

getDirect(X, Y, FireResult, Direct) :-
    (is_vert(X, Y, FireResult), Direct = 1);
    (is_hor(X, Y, FireResult), Direct = 2);
    (isNotFired(X, Y, FireResult), Direct = 0).

/******************************************************/

calculate_next_cell(X, Y, -1, -1, FireResult) :-
    get_possible_point(PosPoint, FireResult, 0, 0),
    length(PosPoint, Len),
    /* write(Len), write("--"), */
    random_between(1, Len, Pos),
    /* write(Pos), nl, */
    nth1(Pos, PosPoint, [X, Y], _).
    /* write("_"),write(X),write("_"),write(Y),nl. */ 

calculate_next_cell(X, Y, OldX, OldY, FireResult) :-
    getDirect(OldX, OldY, FireResult, Direct),
    (
        ( Direct == 1,
          calc_vertical(OldX, OldY, X, Y, FireResult) 
        );
        ( Direct == 2,
          calc_horizon(OldX, OldY, X, Y, FireResult) 
        )
    );
    (
      calc_vertical(OldX, OldY, X, Y, FireResult);
      calc_horizon(OldX, OldY, X, Y, FireResult)
    ).
    /* write("_"),write(X),write("_"),write(Y),nl */
