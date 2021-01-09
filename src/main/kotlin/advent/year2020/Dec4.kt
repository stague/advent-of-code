package advent.year2020

import advent.PuzzleDay

class Dec4: PuzzleDay(4, 2020) {

    override fun puzzle2(): Any = load(delimiter = "\n\n").map { it.replace("\n", " ") + " "}.filter { it.foreignerDetector2PointO() }.size

    // BWAHAHAHA TAKE THAT REGEX YA COCKY BASTARD
    private fun String.foreignerDetector2PointO(): Boolean = matches(Regex("(?=(.*byr:(19[2-9][0-9]|200[0-2]) ))(?=(.*iyr:(201[0-9]|2020) ))(?=(.*hgt:(1[5-8][0-9]cm|19[0-3]cm|59in|6[0-9]in|7[0-6]in) ))(?=(.*eyr:(202[0-9]|2030) ))(?=(.*hcl:#[\\dabcdef]{6} ))(?=(.*ecl:(amb|blu|brn|gry|grn|hzl|oth) ))(?=(.*pid:\\d{9} )).*"))

//
//    fun purgeTheOutsiders(): Int {
//        val m = munger()
//        return munge().filterIndexed { a, b ->
//            b.foreignerDetector2PointO().also {
//                if (it != m[a].foreignerDetector2PointO()) println("V3 mismatch fail: idx $a was $it on val $b\nv3: ${m[a]}")
//            }
//        }.size
//    }
//
//    fun munge(): List<Map<String, String>> =
//        suspects.split("\n\n").map { rawBlob ->
//            rawBlob.replace("\n", " ")
//                .trim()
//                .split(" ")
//                .map { cookedBlob ->
//                    cookedBlob.split(":").let { it[0] to it[1] }
//                }.toMap()
//        }
//
//
//
//    fun Map<String, String>.foreignerDetector2PointO(): Boolean =
//        boop("byr").beep("19[2-9][0-9]|200[0-2]")
//            && boop("iyr").beep("201[0-9]|2020")
//            && boop("eyr").beep("202[0-9]|2030")
//            && boop("hgt").beep("1[5-8][0-9]cm|19[0-3]cm|59in|6[0-9]in|7[0-6]in")
//            && boop("hcl").beep("#[\\dabcdef]{6}")
//            && boop("ecl").beep("amb|blu|brn|gry|grn|hzl|oth")
//            && boop("pid").beep("\\d{9}")
//
//    fun Map<String, String>.boop(k: String): String = get(k) ?: "-1"
//    fun String.beep(r: String): Boolean = matches(Regex(r))
//
//    fun Map<String, String>.foreignerDetector1PointO(): Boolean = size == 8 || (size == 7 && !containsKey("cid"))
}
