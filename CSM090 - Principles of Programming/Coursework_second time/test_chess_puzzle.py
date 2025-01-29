# This will be the same as the other file.
# I do not want to put FLY on every single test since there are too many to do that to.
# But assume all are FLY except the ones with the Queens since that had to be reworked a bit

import pytest
from chess_puzzle import *


def test_location2index1():
    assert location2index("e2") == (5,2)


def test_location2index2():
    assert location2index("a12") == (1, 12)


def test_location2index3():
    assert location2index("z10") == (26, 10)


def test_location2index4():
    assert location2index("j22") == (10, 22)


def test_location2index5():
    assert location2index("f9") == (6, 9)

def test_index2location1():
    assert index2location(5,2) == "e2"


def test_index2location2():
    assert index2location(15, 12) == "o12"


def test_index2location3():
    assert index2location(25, 25) == "y25"


def test_index2location4():
    assert index2location(4, 8) == "d8"


def test_index2location5():
    assert index2location(1, 1) == "a1"


wq1 = Queen(4,4,True)
wk1 = King(3,5,True)
wq2 = Queen(3,1,True)

bq1 = Queen(5,3,False)
bk1 = King(2,3,False)


b1 = (5, [wq1, wk1, wq2, bq1, bk1])

"""
  ♔  
   ♕ 
 ♚  ♛
     
  ♕  
"""


def test_is_valid_move1():
    assert is_valid_move('a1c2', b1) == False


def test_is_valid_move2():
    assert is_valid_move('a12c2', b1) == False


def test_is_valid_move3():
    assert is_valid_move('d4a1', b1) == True


def test_is_valid_move4():
    assert is_valid_move('c5d5', b1) == True


def test_is_valid_move5():
    assert is_valid_move('c5a5', b1) == False


def test_is_piece_at1():
    assert is_piece_at(2, 2, b1) == False


def test_is_piece_at2():
    assert is_piece_at(2, 3, b1) == True


def test_is_piece_at3():
    assert is_piece_at(5, 5, b1) == False


def test_is_piece_at4():
    assert is_piece_at(4, 4, b1) == True


def test_is_piece_at5():
    assert is_piece_at(3, 8, b1) == False


def test_piece_at1():
    assert piece_at(3, 1, b1) == wq2


def test_piece_at2():
    assert piece_at(4, 4, b1) == wq1


def test_piece_at3():
    assert piece_at(2, 3, b1) == bk1


def test_piece_at4():
    assert piece_at(3, 5, b1) == wk1


def test_piece_at5():
    assert piece_at(5, 3, b1) == bq1


def test_can_reach1():
    assert wq1.can_reach(5, 4, b1) == True


def test_can_reach2():
    assert bk1.can_reach(2, 4, b1) == True


def test_can_reach3():
    assert bk1.can_reach(1, 1, b1) == False


def test_can_reach4():
    assert wk1.can_reach(1, 3, b1) == False


def test_can_reach5():
    assert wq1.can_reach(2, 8, b1) == False
    

def test_can_move_to1():
    assert wq1.can_move_to(5, 4, b1) == False


def test_can_move_to2():
    assert bk1.can_move_to(1, 2, b1) == True


def test_can_move_to3():
    assert bk1.can_move_to(1, 3, b1) == False


def test_can_move_to4():
    assert wk1.can_move_to(2, 3, b1) == False


def test_can_move_to5():
    assert bq1.can_move_to(2, 3, b1) == False


def test_location_occupied1():
    assert location_occupied(4, 5, b1) == (False, None)


def test_location_occupied2():
    assert location_occupied(3, 5, b1) == (True, wk1)


def test_location_occupied3():
    assert location_occupied(2, 5, b1) == (False, None)


def test_location_occupied4():
    assert location_occupied(2, 3, b1) == (True, bk1)


def test_location_occupied5():
    assert location_occupied(5, 3, b1) == (True, bq1)


def test_move_to1():
    wk1a = King(4,5, True)

    actual_b = wk1.move_to(4, 5, b1)
    expected_b = (5, [wq1, wk1a, wq2, bq1, bk1])
    # check if actual board has same contents as expected
    assert actual_b[0] == 5

    for piece1 in actual_b[1]:  # we check if every piece in actual_b is also present in expected_b; if not, the test will fail
        found = False
        for piece in expected_b[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and type(piece) == type(piece1):
                found = True
        assert found


    for piece in expected_b[1]:  # we check if every piece in expected_b is also present in actual_b; if not, the test will fail
        found = False
        for piece1 in actual_b[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and type(piece) == type(piece1):
                found = True
        assert found


def test_is_check1():
    wk1 = King(3, 5, True)
    wq2 = Queen(3, 1, True)
    
    bq1 = Queen(5, 3, False)
    bk1 = King(2, 3, False)
    b2 = (5, [wk1, wq2, bq1, bk1])
    assert is_check(True, b2) == True


def test_is_check2():
    assert is_check(True, b1) == False


def test_is_check3():
    wq1a = Queen(4, 5, True)
    b2 = (5, [wq1a, wq2, wk1, bq1, bk1])

    """
             ♔      ♕    
                       
         ♚             ♛
                     
             ♕         
    """

    assert is_check(False, b2) == True


def test_is_check4():
    wq1a = Queen(4, 5, True)
    b2 = (5, [wq1a, wq2, wk1, bq1, bk1])

    """
             ♔      ♕    
                       
         ♚             ♛
                     
             ♕         
    """

    assert is_check(True, b2) == False


def test_is_check5():
    assert is_check(False, b1) == False


def test_is_stalemate1():
    wk1a = King(3, 3, True)
    bk1a = King(1, 1, False)
    bq1a = Queen(1, 2, False)
    bq2a = Queen(2, 1, False)
    bq3a = Queen(4, 5, False)
    bq4a = Queen(5, 4, False)
    b3 = (5, [wk1a, bk1a, bq1a, bq2a, bq3a, bq4a])

    '''
       ♛ 
        ♛
      ♔  
    ♛    
    ♚♛   
    '''
    assert is_stalemate(True, b3) == True


def test_is_stalemate2():
    assert is_stalemate(True, b1) == False


def test_is_stalemate3():
    wq1a = Queen(4, 5, True)
    b2 = (5, [wq1a, wq2, wk1, bq1, bk1])

    """
             ♔      ♕    
                       
         ♚             ♛
                     
             ♕         
    """

    assert is_stalemate(False, b2) == False


def test_is_checkmate1():
    b2 = (5, [wk1, wq2, bq1, bk1])
    assert is_checkmate(True, b2) == False


def test_split_move1():
    assert split_move("e2f2") == ("e2", "f2")


def test_split_move2():
    assert split_move("o12j35") == ("o12", "j35")


def test_split_move3():
    assert split_move("y25w77") == ("y25", "w77")


def test_split_move4():
    assert split_move("d8n2") == ("d8", "n2")


def test_split_move5():
    assert split_move("a1q61") == ("a1", "q61")


def test_from_file_to_piece1():
    with pytest.raises(IOError):
        from_file_to_piece('Aa2', True, 5)


def test_from_file_to_piece2():
    with pytest.raises(IOError):
        from_file_to_piece('Ne10', True, 5)


def test_from_file_to_piece3():
    q = Queen(5, 4, True)
    fp = from_file_to_piece('Qe4', True, 5)

    found = False
    if fp.pos_x == q.pos_x and fp.pos_y == q.pos_y and fp.side == q.side and \
            type(fp) == type(q):
        found = True
    assert found


def test_from_file_to_piece4():
    k = King(2, 3, False)
    fp = from_file_to_piece('Kb3', False, 5)

    found = False
    if fp.pos_x == k.pos_x and fp.pos_y == k.pos_y and fp.side == k.side and \
            type(fp) == type(k):
        found = True
    assert found


def test_from_file_to_piece5():
    with pytest.raises(IOError):
        from_file_to_piece('Kz5', True, 5)


def test_read_board1():
    # resetting board in case it changed from prior tests
    wq1 = Queen(4, 4, True)
    wk1 = King(3, 5, True)
    wq2 = Queen(3, 1, True)
    
    bq1 = Queen(5, 3, False)
    bk1 = King(2, 3, False)
    
    b1 = (5, [wq1, wk1, wq2, bq1, bk1])
    
    b = read_board("board_examp.txt")
    assert b[0] == 5

    for piece in b[1]:  # we check if every piece in B is also present in B1; if not, the test will fail
        found = False
        for piece1 in b1[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and type(piece) == type(piece1):
                found = True
        assert found

    for piece1 in b1[1]:  # we check if every piece in B1 is also present in B; if not, the test will fail
        found = False
        for piece in b[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and type(piece) == type(piece1):
                found = True
        assert found


def test_read_board2():
    with pytest.raises(IOError):
        brd = read_board("board_examp5.txt")


def test_read_board3():
    with pytest.raises(IOError):
        brd = read_board("board_examp1.txt")


def test_read_board4():
    with pytest.raises(IOError):
        brd = read_board("board_examp2.txt")


def test_read_board5():
    with pytest.raises(IOError):
        brd = read_board("board_examp3.txt")


def test_conf2unicode1():
    wq1 = Queen(4, 4, True)
    wk1 = King(3, 5, True)
    wq2 = Queen(3, 1, True)
    
    bq1 = Queen(5, 3, False)
    bk1 = King(2, 3, False)
    
    b1 = (5, [wq1, wk1, wq2, bq1, bk1])
    
    assert conf2unicode(b1).rstrip("\n") == "  ♔  \n   ♕ \n ♚  ♛\n     \n  ♕  "