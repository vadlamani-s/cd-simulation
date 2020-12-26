import sys
import collections

"""
The Code class has all the methods for simulating the cd command of UNIX.
"""


class Commandline(object):
    """
    The constructor instantiates an object of the code class with the current directory as one of
    the parameters.
    """

    def __init__(self, directory):
        if directory is None or directory == "":
            raise Exception("invalid current directory")
        self.current_directory = directory
        self.new_directory = ""
        self.map = self.load_map()

    def load_map(self):
        map = collections.defaultdict()
        map[""] = self.process_empty
        map["."] = self.process_period
        map[".."] = self.process_parent
        return map

    def process(self, c_string):
        if c_string == "":
            self.new_directory = self.current_directory
            return self.new_directory
        c_string = self._set_new_directory(c_string)
        lst = c_string.split('/') if '/' in c_string else ["", c_string]
        for i in range(1, len(lst)):
            function = self.map.get(lst[i])
            if function:
                function()
            else:
                if not lst[i].isalnum():
                    raise Exception("No such file or Directory")
                self.process_directory(lst[i])
        return self.new_directory

    def _set_new_directory(self, c_string):
        if c_string.startswith('../') or c_string == '..':
            directory = '/' + self.current_directory.split('/')[-1]
            self.new_directory = self.current_directory[:-len(directory)]
            if '/' not in self.new_directory:
                self.new_directory = '/'
            c_string = c_string.replace("..", "", 1)
        elif c_string.startswith('./') or c_string == '.':
            self.new_directory = self.current_directory
            c_string = c_string.replace(".", "", 1)
        elif c_string.startswith('/'):
            self.new_directory = '/'
        else:
            self.new_directory = self.current_directory
        return c_string

    def process_empty(self):
        pass

    def process_period(self):
        pass

    def process_parent(self):
        directory = '/' + self.new_directory.split('/')[-1]
        self.new_directory = self.new_directory[:-len(directory)]
        if '/' not in self.new_directory:
            self.new_directory = '/'

    def process_directory(self, directory):
        if self.new_directory[-1] != '/':
            self.new_directory += '/'
        self.new_directory += directory


if __name__ == "__main__":
    current_directory = ""
    command_string = ""
    current_directory = sys.argv[1]
    command_string = sys.argv[2]
    command = Commandline(current_directory)
    print(command.process(command_string))
