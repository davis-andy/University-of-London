extern crate getopts;
use std::{env, fs::File, process};
use std::io::{ BufRead, BufReader };
use getopts::Options;

// Print out errors for command line flags
fn print_usage(program: &str, opts: &Options) {
    let brief = format!("{}: Missing required command line argument\n\
    Usage: {} [-hv] -s <num> -E <num> -b <num> -t <file>", program, program);
    println!("{}", opts.usage(&brief));
    println!("Examples:");
    println!("    {program} -s 4 -E 1 -b 4 -t traces/yi.trace");
    println!("    {program} -v -s 8 -E 2 -b 4 -t traces/yi.trace");
    process::exit(0);
}

// parsing the command line arguments
fn optional_flags(args: &[String]) -> (bool, usize, usize, usize, String){
    let program = args[0].clone();
    let mut index = 0;
    let mut lines = 0;
    let mut bits = 0;
    let mut verbose= false;
    let mut trace = "None".to_string();

    let mut opts = Options::new();
    opts.optflag("h", "", "Print this help message.");
    opts.optflag("v", "", "Optional verbose flag.");
    opts.optopt("s", "", "Number of set index bits.", "<num>");
    opts.optopt("E", "", "Number of lines per set.", "<num>");
    opts.optopt("b", "", "Number of block bits.", "<num>");
    opts.optopt("t", "", "Trace file.", "<file>");

    let matches = match opts.parse(&args[1..]) {
        Ok(m) => { m }
        Err(f) => { panic!("{}", f.to_string()) }
    };

    if matches.opt_present("h") { print_usage(&program, &opts); }  // help flag

    if matches.opt_present("v") { verbose = true; } // verbose flag

    if matches.opt_present("s") {  // set bits flag
        let set_index = matches.opt_str("s").unwrap();
        index = set_index.parse().unwrap();
    } else {print_usage(&program, &opts)}

    if matches.opt_present("E") {    // associativity flag
        let set_lines = matches.opt_str("E").unwrap();
        lines = set_lines.parse().unwrap();
    } else {print_usage(&program, &opts)}

    if matches.opt_present("b") {  // block bits flag
        let block_bits = matches.opt_str("b").unwrap();
        bits = block_bits.parse().unwrap();
    } else {print_usage(&program, &opts)}

    if matches.opt_present("t") {  // trace file flag
        trace = matches.opt_str("t").unwrap();
    } else {print_usage(&program, &opts)}

    (verbose, index, lines, bits, trace)
}

// establish the classes
#[derive(Clone)]
struct Blocks {
    valid: bool,
    tag: usize,
}

impl Blocks {
    fn new() -> Self {
        Blocks {
            valid: false,
            tag: 0,
        }
    }
}

#[derive(Clone)]
struct Sets {
    set: Vec<Blocks>,
}

impl Sets {
    // from -E flag
    fn new(num_of_lines: usize) -> Self {
        Sets {
            set: vec![Blocks::new(); num_of_lines],
        }
    }
}

struct Cache {
    set_bits: usize,  // from -s flag
    block_bits: usize,  // from -b flag
    num_of_sets: usize,
    cache_structure: Vec<Sets>,
    hits: usize,
    misses: usize,
    evictions: usize,
    event: String,
}

impl Cache {
    fn new(set_bits: usize, num_of_lines: usize, block_bits: usize) -> Self {
        let num_of_sets = 1 << set_bits;
        let mut cache_structure = Vec::with_capacity(num_of_sets);
        for _i in 0..num_of_sets {
            cache_structure.push(Sets::new(num_of_lines));
        }

        Cache {
            set_bits,
            block_bits,
            num_of_sets,
            cache_structure,
            hits: 0,
            misses: 0,
            evictions: 0,
            event: "".to_owned()
        }
    }

    // function that does all the work
    fn check_cache(&mut self, address: &usize) {
        // with help from https://coffeebeforearch.github.io/2020/12/16/cache-simulator.html
        // and a little from https://github.com/bulbazord/cache-sim
        // get information from address
        let addr_set_number = ((address >> self.block_bits) & (self.num_of_sets - 1)) as usize;
        let addr_tag = address >> (self.set_bits + self.block_bits);

        // get corresponding set from addr_set_number
        let cache_set = &mut self.cache_structure[addr_set_number].set;

        // checker variables
        let mut hit = false;
        let mut invalid_index = usize::MAX;

        // iterate through the lines in each set
        for (idx, line) in cache_set.iter_mut().enumerate() {
            // line is valid and tag matches address tag, then hit
            if line.valid && line.tag == addr_tag {
                hit = true;
                self.hits += 1;
                self.event.push_str(" hit");
                break;
            }
            // to change info of block
            if idx < invalid_index { invalid_index = idx; }
        }

        if !hit {
            self.misses += 1;
            self.event.push_str(" miss");
            if cache_set[invalid_index].valid {
                self.evictions += 1;
                self.event.push_str(" eviction");
            };
            cache_set[invalid_index].valid = true;
            cache_set[invalid_index].tag = addr_tag;
        }
    }
}

// essentially the run function
pub fn cache_sim(file: impl BufRead, flags: (bool, usize, usize, usize, String)) {
    // initialize cache
    let mut cache = Cache::new(flags.1, flags.2, flags.3);

    // read each line of the file
    for line in file.lines() {
        // for verbose reasons
        cache.event = "".to_owned();

        // split each line into a list
        let line_unwrap = line.unwrap();
        let mut unwrap_split = line_unwrap.split_whitespace();

        // Operation is either L, M, S, or I and at beginning of line
        let operation = unwrap_split.next().unwrap();

        // If Operation is I, no need to continue, so restart loop
        if operation.contains("I") {
            continue;
        }

        // after Operation, get the rest of the line
        let after_operation = unwrap_split.next().unwrap();
        // Only care for the address part
        let address = after_operation.split(",").next().unwrap();
        let hex_address = usize::from_str_radix(&address, 16).unwrap();

        // now the fun part
        match operation {
            // Load and Store act the same way
            "L" | "S" => cache.check_cache(&hex_address),

            "M" => {
                // M is both Load and Store aka check cache twice
                // first one will be an actual check, second one is always a hit
                cache.check_cache(&hex_address);

                // add the second hit event
                cache.hits += 1;
                cache.event.push_str(" hit");
            }

            // Default, nothing happens
            _ => ()
        }

        // if verbose, print out lines
        if flags.0 {
            println!("{operation} {after_operation}{}", cache.event);
        }
    }
    // finally report findings
    println!("hits:{} misses:{} evictions:{}", cache.hits, cache.misses, cache.evictions);
}


pub fn main() {
    // collect command line arguments
    let args: Vec<String> = env::args().collect();
    let flags = optional_flags(&args);  // 1 = set bits, 2 = number of lines, 3 = block bits

    // attempt to open the file
    let file = File::open(&flags.4).unwrap_or_else(|_e| {
        println!("Cannot open file {}", flags.4);
        process::exit(0);
    });

    // file handler variable
    let trace_file = BufReader::new(file);

    // run the sim
    cache_sim(trace_file, flags);
}