import json
import socket
import sqlite3
import threading

SERVER_CONFIG = ("0.0.0.0", 6000)
PACKET_SIZE = 1024


def handling_request(client_socket: socket.socket, json_struct):
    """
    the function handles the request
    :param client_socket: socket
    :param json_struct: dict
    :return:
    """
    db_connection = sqlite3.connect('database_sql.db')
    cursor = db_connection.cursor()

    if json_struct["request"] == "login":
        email = str(json_struct["email"])
        password = str(json_struct["password"])

        query = f"SELECT * FROM all_users WHERE email = '{email}'"
        answer = cursor.execute(query)
        db_connection.commit()
        if answer.fetchone() is None:
            client_socket.send(json.dumps({"response": "login failed"}).encode())
        else:
            data_password = cursor.execute(f"SELECT password FROM all_users WHERE email = '{email}'")
            db_connection.commit()
            data_password = data_password.fetchone()[0]
            print(data_password)
            if data_password == password:
                client_socket.send(json.dumps({"response": "login succeeded"}).encode())
            else:
                client_socket.send(json.dumps({"response": "login failed"}).encode())

    elif json_struct["request"] == "register":
        email = str(json_struct["email"])
        password = str(json_struct["password"])
        username = str(json_struct["username"])
        insert_query = f"INSERT into all_users VALUES('{email}', '{username}', '{password}', 0,0,0)"
        try:
            cursor.execute(insert_query)
            db_connection.commit()
            client_socket.send(json.dumps({"response": "register succeeded"}).encode())
        except sqlite3.IntegrityError as unique_exception:
            print(f"{unique_exception}, User already exists")
            client_socket.send(json.dumps({"response": "register failed"}).encode())
    elif json_struct["request"] == "saveEasy":
        score = int(json_struct["score"])
        email = str(json_struct["email"])
        data_score = cursor.execute(f"SELECT scoreEasy FROM all_users WHERE email = '{email}'")
        db_connection.commit()
        data_score = data_score.fetchone()[0]

        if score > data_score:
            try:
                updateQuery = f"UPDATE all_users SET scoreEasy = {score} WHERE email='{email}'"
                cursor.execute(updateQuery)
                db_connection.commit()
            finally:
                check = cursor.execute('''SELECT * from all_users ORDER BY scoreEasy DESC''')
                db_connection.commit()
                high = check.fetchone()
                dict = {"response": "updated", "score": high[3], "username": high[1]}

        else:
            dict = {"response": "not updated"}
        client_socket.send(json.dumps(dict).encode())

    elif json_struct["request"] == "saveMedium":
        score = int(json_struct["score"])
        email = str(json_struct["email"])
        data_score = cursor.execute(f"SELECT scoreMedium FROM all_users WHERE email = '{email}'")
        db_connection.commit()
        data_score = data_score.fetchone()[0]

        if score > data_score:
            try:
                updateQuery = f"UPDATE all_users SET scoreMedium = {score} WHERE email='{email}'"
                cursor.execute(updateQuery)
                db_connection.commit()
            finally:
                check = cursor.execute('''SELECT * from all_users ORDER BY scoreMedium DESC''')
                db_connection.commit()
                high = check.fetchone()
                dict = {"response": "updated", "score": high[4], "username": high[1]}

        else:
            dict = {"response": "not updated"}
        client_socket.send(json.dumps(dict).encode())

    elif json_struct["request"] == "saveHard":
        score = int(json_struct["score"])
        email = str(json_struct["email"])
        data_score = cursor.execute(f"SELECT scoreHard FROM all_users WHERE email = '{email}'")
        db_connection.commit()
        data_score = data_score.fetchone()[0]

        if score > data_score:
            try:
                updateQuery = f"UPDATE all_users SET scoreHard = {score} WHERE email='{email}'"
                cursor.execute(updateQuery)
                db_connection.commit()
            finally:
                check = cursor.execute('''SELECT * from all_users ORDER BY scoreHard DESC''')
                db_connection.commit()
                high = check.fetchone()
                dict = {"response": "updated", "score": high[5], "username": high[1]}

        else:
            dict = {"response": "not updated"}
        client_socket.send(json.dumps(dict).encode())

    elif json_struct["request"] == "getEasy":
        stam = cursor.execute(f"SELECT username , scoreEasy FROM all_users ORDER BY scoreEasy DESC")
        db_connection.commit()
        client_socket.send(json.dumps({"scores": stam.fetchall()}).encode())
        print("kkk")

    elif json_struct["request"] == "getMedium":
        stam = cursor.execute(f"SELECT username , scoreMedium FROM all_users ORDER BY scoreMedium DESC")
        db_connection.commit()
        client_socket.send(json.dumps({"scores": stam.fetchall()}).encode())

    elif json_struct["request"] == "getHard":
        stam = cursor.execute(f"SELECT username , scoreHard FROM all_users ORDER BY scoreHard DESC")
        db_connection.commit()
        sent = json.dumps({"scores": stam.fetchall()}).encode()
        client_socket.send(sent)
    elif json_struct["request"] == "username_request":
        email = json_struct["email"]
        stam = cursor.execute(f"SELECT username FROM all_users WHERE email = '{email}'")
        db_connection.commit()
        try:
            stam = str(stam.fetchone()[0])
        except:
            stam = "  "
        sent = json.dumps({"username": stam})
        client_socket.send(sent.encode())


def new_connection_receiver(client_socket: socket.socket, ip_address: str):
    """
    :param client_socket: connection
    :param ip_address: address
    :return:
    """
    print(f"New connection from {ip_address}, via: {client_socket}")
    request = client_socket.recv(PACKET_SIZE)
    decoded = request.decode()
    print(decoded)

    json_struct = json.loads(decoded)
    print(json_struct)

    handling_request(client_socket, json_struct)
    client_socket.close()


def main():
    db_connection = sqlite3.connect('database_sql.db')
    cursor = db_connection.cursor()

    cursor.execute(
        '''CREATE TABLE IF NOT EXISTS all_users(Email text UNIQUE, Username text UNIQUE, Password text, ScoreEasy int, ScoreMedium int, ScoreHard int)''')
    db_connection.commit()

    print("Starts server")
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    server_socket.bind(SERVER_CONFIG)
    server_socket.listen()  # Listens for all the connections

    while True:
        new_client = server_socket.accept()
        new_thread = threading.Thread(target=new_connection_receiver, args=new_client)
        print("gamer")

        new_thread.start()


if __name__ == '__main__':
    main()
