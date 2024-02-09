import requests
from bs4 import BeautifulSoup
from PIL import Image, ImageFilter,ImageOps
import time
from concurrent.futures import ProcessPoolExecutor, as_completed



def justSaveLink(href,address):
    if not href.endswith(".png") :
        return 
    r=requests.get(address+href).content
    with open(href,'wb') as f:
        f.write(r)
    print(href,flush=True)
    return href


def doStuffToLink(href,address):
    if not href.endswith(".png") :
        return 
    
    # r=requests.get(address+href).content
    # with open(href,'wb') as f:
    #     f.write(r)
    img=Image.open(href)
    # img=img.convert("1")
    # img=ImageOps.grayscale(img)#zeby sie dalo nadac gaussianblur
    # img=img.filter(ImageFilter.GaussianBlur(radius=10))
    img=img.filter(ImageFilter.Kernel((3, 3), 
      (1.0/16, 1.0/8, 1.0/16, 1.0/8, 1.0/4, 1.0/8, 1.0/16, 1.0/8, 1.0/16), 1, 0))         
    img.save(href)
    print(href,flush=True)
    return href



if __name__ == '__main__':
    address='http://www.if.pw.edu.pl/~mrow/dyd/wdprir/'
    req = requests.get(address)
    soup = BeautifulSoup(req.text, 'html.parser')

    print("Samo pobieranie")
    start = time.time()
    for entry in soup.find_all("a",href=True):
        justSaveLink(entry['href'],address)
    stop = time.time()            
    print(f'W wersji jednowątkowej wykonało się w {stop - start}s') 

    start = time.time()
    with ProcessPoolExecutor(24) as ex:
        futures = [ex.submit(justSaveLink, entry['href'],address) for entry in soup.find_all("a",href=True)]
        for f in as_completed(futures): 
            pass
    stop = time.time()            
    print(f'W wersji wielowątkowej wykonało się w {stop - start}s') 

    print("Blur")
    start = time.time()
    for entry in soup.find_all("a",href=True):
        doStuffToLink(entry['href'],address)
    stop = time.time()            
    print(f'W wersji jednowątkowej wykonało się w {stop - start}s') 

    start = time.time()
    with ProcessPoolExecutor(24) as ex:
        futures = [ex.submit(doStuffToLink, entry['href'],address) for entry in soup.find_all("a",href=True)]
        for f in as_completed(futures): 
            pass
    stop = time.time()            
    print(f'W wersji wielowątkowej wykonało się w {stop - start}s') 
