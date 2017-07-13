from PIL import Image
import sys

def iter_frames(im):
    try:
        i= 0
        while 1:
            im.seek(i)
            imframe = im.copy()
            if i == 0: 
                palette = imframe.getpalette()
            else:
                imframe.putpalette(palette)
            yield imframe
            i += 1
    except EOFError:
        pass

if (__name__ == "__main__")  :
	print(sys.argv)
	arg = sys.argv[1]
	im = Image.open(arg)
	for i, frame in enumerate(iter_frames(im)):
    		frame.save('test%d.png' % i,**frame.info)
