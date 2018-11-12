def load(casos, path):
    with open(path, 'r') as f:
        subs, i = [], 0
        for x in casos:
            i += 1
            subs.append(["test {:d}".format(i)])
            i %= 10

        cliente, cont, indice, estado = 1, 0, 0, 0
        for x in f.readlines():
            if estado == 0:
                if x == ("->delegado {:d}: \n".format(cliente)):
                    cliente += casos[indice] - 1
                    indice += 1
                    estado = 1
            elif estado == 1:
                if x.startswith('<') or x.startswith('-'):
                    if x == ("<-delegado {:d}: \n".format(cliente)):
                        estado = 2
                else:
                    subs[indice - 1].append(x[:-1].replace('.', ','))
            else:
                if cont < 40:
                    if not (x.startswith('<') or x.startswith('-')):
                        subs[indice - 1].append(x[:-1].replace('.', ','))
                else:
                    cliente += 1
                    estado, cont = 0, 0
                cont += 1
    return subs


def print(casos, subs, path):
    with open(path, 'w') as w:
        w.write(';'.join(list(map(lambda i: str(i), casos))) + "\n")
        sale, line = False, 0
        while not sale:
            t, sale = [], True
            for i in range(len(casos)):
                if line < len(subs[i]):
                    t.append(subs[i][line])
                    sale &= False
                else:
                    t.append(' ')
            w.write(';'.join(t) + "\n")
            line += 1


x = 10
casos = [400]*x + [200]*x + [80]*x
mat = load(casos, "seguridad/1ThreadServidor.txt")
print(casos, mat, 'seguridad/1ThreadServidor.csv')
