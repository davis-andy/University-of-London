import pytest
from chess_puzzle import *


def test_location2index1():
    assert location2index("e2") == (5, 2)


def test_location2index2():
    assert location2index("a12") == (1, 12)


def test_location2index3():
    assert location2index("z10") == (26, 10)


def test_location2index4():
    assert location2index("j22") == (10, 22)


def test_location2index5():
    assert location2index("f9") == (6, 9)


def test_index2location1():
    assert index2location(5, 2) == "e2"


def test_index2location2():
    assert index2location(15, 12) == "o12"


def test_index2location3():
    assert index2location(25, 25) == "y25"


def test_index2location4():
    assert index2location(4, 8) == "d8"


def test_index2location5():
    assert index2location(1, 1) == "a1"


wn1 = Knight(1, 2, True)
wn2 = Knight(5, 2, True)
wn3 = Knight(5, 4, True)
wk1 = King(3, 5, True)

bn1 = Knight(1, 1, False)
bk1 = King(2, 3, False)
bn2 = Knight(2, 4, False)

b1 = (5, [wn1, bn1, wn2, bn2, wn3, wk1, bk1])

'''
  ♔  
 ♞  ♘
 ♚   
♘   ♘
♞    
'''


def test_is_valid_move1():
    assert is_valid_move('a1c2', b1) == True


def test_is_valid_move2():
    assert is_valid_move('a12c2', b1) == False


def test_is_valid_move3():
    assert is_valid_move('a2c2', b1) == False


def test_is_valid_move4():
    assert is_valid_move('a2c1', b1) == True


def test_is_valid_move5():
    assert is_valid_move('b3c5', b1) == False


def test_is_piece_at1():
    assert is_piece_at(2, 2, b1) == False


def test_is_piece_at2():
    assert is_piece_at(2, 3, b1) == True


def test_is_piece_at3():
    assert is_piece_at(5, 5, b1) == False


def test_is_piece_at4():
    assert is_piece_at(2, 4, b1) == True


def test_is_piece_at5():
    assert is_piece_at(3, 3, b1) == False


def test_piece_at1():
    assert piece_at(1, 1, b1) == bn1


def test_piece_at2():
    assert piece_at(1, 2, b1) == wn1


def test_piece_at3():
    assert piece_at(2, 3, b1) == bk1


def test_piece_at4():
    assert piece_at(3, 5, b1) == wk1


def test_piece_at5():
    assert piece_at(5, 4, b1) == wn3


def test_can_reach1():
    assert bn1.can_reach(2, 2, b1) == False


def test_can_reach2():
    assert bk1.can_reach(2, 4, b1) == False


def test_can_reach3():
    assert bk1.can_reach(1, 3, b1) == True


def test_can_reach4():
    assert wk1.can_reach(1, 3, b1) == False


def test_can_reach5():
    assert wn1.can_reach(2, 4, b1) == True


def test_can_move_to1():
    assert wk1.can_move_to(4, 5, b1) == False


def test_can_move_to2():
    assert wk1.can_move_to(2, 5, b1) == True


def test_can_move_to3():
    assert wn1.can_move_to(2, 5, b1) == False


def test_can_move_to4():
    assert bn1.can_move_to(2, 3, b1) == False


def test_can_move_to5():
    assert bn1.can_move_to(3, 2, b1) == True


def test_location_occupied1():
    assert location_occupied(4, 5, b1) == (False, None)


def test_location_occupied2():
    assert location_occupied(3, 5, b1) == (True, wk1)


def test_location_occupied3():
    assert location_occupied(2, 5, b1) == (False, None)


def test_location_occupied4():
    assert location_occupied(2, 3, b1) == (True, bk1)


def test_location_occupied5():
    assert location_occupied(1, 1, b1) == (True, bn1)


def test_move_to1():
    actual_b = wn1.move_to(2, 4, b1)
    wn1a = Knight(2, 4, True)
    expected_b = (5, [wn1a, bn1, wn2, wn3, wk1, bk1])

    '''
      ♔   
     ♘  ♘
     ♚   
        ♘
    ♞    
    '''

    # check if actual board has same contents as expected
    assert actual_b[0] == 5

    # we check if every piece in Actual_B is also present in Expected_B; if not, the test will fail
    for piece1 in actual_b[1]:
        found = False
        for piece in expected_b[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and type(piece) == type(piece1):
                found = True
        assert found

    # we check if every piece in Expected_B is also present in Actual_B; if not, the test will fail
    for piece in expected_b[1]:
        found = False
        for piece1 in actual_b[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and type(piece) == type(piece1):
                found = True
        assert found


def test_is_check1():
    wk1a = King(4, 5, True)
    b2 = (5, [wn1, bn1, wn2, bn2, wn3, wk1a, bk1])

    '''
       ♔ 
     ♞  ♘
     ♚   
    ♘   ♘
    ♞    
    '''
    
    assert is_check(True, b2) == True


def test_is_check2():
    assert is_check(True, b1) == False


def test_is_check3():
    wk1a = King(4, 5, True)
    b2 = (5, [wn1, bn1, wn2, bn2, wn3, wk1a, bk1])

    '''
       ♔ 
     ♞  ♘
     ♚   
    ♘   ♘
    ♞    
    '''

    assert is_check(False, b2) == False


def test_is_check4():
    wk1a = King(5, 5, True)
    b3 = (5, [wn1, bn1, wn2, bn2, wn3, wk1a, bk1])

    '''
        ♔
     ♞  ♘
     ♚   
    ♘   ♘
    ♞    
    '''

    assert is_check(True, b3) == False


def test_is_check5():
    bk1a = King(3, 3, False)
    b4 = (5, [wn1, bn1, wn2, bn2, wn3, wk1, bk1a])

    '''
       ♔ 
     ♞  ♘
      ♚  
    ♘   ♘
    ♞    
    '''

    assert is_check(False, b4) == True


def test_is_checkmate1():
    wk1a = King(1, 5, True)
    bn2a = Knight(3, 4, False)
    bn3 = Knight(4, 4, False)
    b2 = (5, [wn1, wn2, wn3, wk1a, bn1, bk1, bn2a, bn3])
  
    '''
    ♔    
      ♞♞♘
     ♚   
    ♘   ♘
    ♞    
    '''
    assert is_checkmate(True, b2) == True


def test_is_checkmate2():
    wk1a = King(1, 5, True)
    bn2a = Knight(3, 4, False)
    bn3 = Knight(4, 4, False)
    b2 = (5, [wn1, wn2, wn3, wk1a, bn1, bk1, bn2a, bn3])

    '''
    ♔    
      ♞♞♘
     ♚   
    ♘   ♘
    ♞    
    '''
    assert is_checkmate(False, b2) == False


def test_is_checkmate3():
    wk1a = King(1, 5, True)
    bn2a = Knight(3, 4, False)
    bn3 = Knight(2, 4, False)
    b2 = (5, [wn1, wn2, wn3, wk1a, bn1, bk1, bn2a, bn3])

    '''
    ♔    
     ♞♞ ♘
     ♚   
    ♘   ♘
    ♞    
    '''
    assert is_checkmate(True, b2) == False


def test_is_checkmate4():
    wk1a = King(1, 5, True)
    b2 = (5, [wk1a, bk1])

    '''
    ♔    
         
     ♚   
         
         
    '''
    assert is_checkmate(True, b2) == False


def test_is_checkmate5():
    wk1a = King(1, 5, True)
    bn2a = Knight(3, 4, False)
    b2 = (5, [wk1a, bk1, bn2a])

    '''
    ♔    
      ♞  
     ♚   
         
         
    '''
    assert is_checkmate(True, b2) == False


def test_is_stalemate1():
    wk1a = King(1, 5, True)
    bn2a = Knight(3, 4, False)
    bn3 = Knight(4, 4, False)
    b2 = (5, [wn1, wn2, wn3, wk1a, bn1, bk1, bn2a, bn3])

    '''
    ♔    
      ♞♞♘
     ♚   
    ♘   ♘
    ♞    
    '''
    assert is_stalemate(True, b2) == False


def test_is_stalemate2():
    wk1a = King(1, 5, True)
    bn2a = Knight(1, 3, False)
    bn3 = Knight(3, 5, False)
    b2 = (5, [wk1a, bk1, bn2a, bn3])

    '''
    ♔ ♞  
         
    ♞♚   
         
         
    '''
    assert is_stalemate(True, b2) == True


def test_is_stalemate3():
    bk1a = King(3, 3, False)
    wn1a = Knight(5, 5, True)
    wn2a = Knight(5, 3, True)
    wn3a = Knight(1, 1, True)
    wn4a = Knight(2, 3, True)
    wn5a = Knight(3, 4, True)
    b2 = (5, [wn1a, wn2a, wn3a, wn4a, wn5a, wk1, bk1a])

    '''
      ♔ ♘
      ♘  
     ♘♚ ♘
         
    ♘    
    '''
    assert is_stalemate(False, b2) == True


def test_is_stalemate4():
    bk1a = King(3, 3, False)
    wn1a = Knight(5, 5, True)
    wn2a = Knight(5, 3, True)
    wn3a = Knight(1, 3, True)
    wn4a = Knight(2, 3, True)
    wn5a = Knight(3, 4, True)
    b2 = (5, [wn1a, wn2a, wn3a, wn4a, wn5a, wk1, bk1a])

    '''
      ♔ ♘
      ♘  
     ♘♚ ♘
         
      ♘  
    '''
    assert is_stalemate(False, b2) == False


def test_is_stalemate5():
    wk1a = King(1, 5, True)
    b2 = (5, [wk1a, bk1])

    '''
    ♔    
         
     ♚   
         
         
    '''
    assert is_stalemate(True, b2) == False


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
    n = Knight(5, 4, True)
    fp = from_file_to_piece('Ne4', True, 5)

    found = False
    if fp.pos_x == n.pos_x and fp.pos_y == n.pos_y and fp.side == n.side and \
            type(fp) == type(n):
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


def test_can_capture1():
    wk1a = King(1, 5, True)
    bn2a = Knight(3, 4, False)
    bn3 = Knight(4, 4, False)
    b2 = (5, [wn1, wn2, wn3, wk1a, bn1, bk1, bn2a, bn3])

    '''
    ♔    
      ♞♞♘
     ♚   
    ♘   ♘
    ♞    
    '''
    assert can_capture(True, b2) == (False, None, 0, 0)


def test_can_capture2():
    wk1a = King(1, 5, True)
    bn2a = Knight(3, 4, False)
    bn3 = Knight(4, 4, False)
    b2 = (5, [wn1, wn2, wn3, wk1a, bn1, bk1, bn2a, bn3])

    '''
    ♔    
      ♞♞♘
     ♚   
    ♘   ♘
    ♞    
    '''
    assert can_capture(False, b2) == (True, bn2a, 1, 5)


def test_read_board1():
    brd = read_board("board_examp.txt")
    assert brd[0] == 5

    # had to repeat the test board down here due to error not reading it correctly from above
    whn1 = Knight(1, 2, True)
    whn2 = Knight(5, 2, True)
    whn3 = Knight(5, 4, True)
    whk1 = King(3, 5, True)

    bln1 = Knight(1, 1, False)
    blk1 = King(2, 3, False)
    bln2 = Knight(2, 4, False)

    brd1 = (5, [whn1, bln1, whn2, bln2, whn3, whk1, blk1])

    for piece in brd[1]:  # we check if every piece in B is also present in B1; if not, the test will fail
        found = False
        for piece1 in brd1[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and \
                    type(piece) == type(piece1):
                found = True
        assert found

    for piece1 in brd1[1]:  # we check if every piece in B1 is also present in B; if not, the test will fail
        found = False
        for piece in brd[1]:
            if piece.pos_x == piece1.pos_x and piece.pos_y == piece1.pos_y and piece.side == piece1.side and \
                    type(piece) == type(piece1):
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
