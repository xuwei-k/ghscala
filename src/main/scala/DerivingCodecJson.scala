package ghscala

import argonaut._, CodecJson._

abstract class DerivingCodecJson2[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, X](an: String, bn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec2(apply, unapply)(an, bn)

  def apply(a: A, b: B): X

  def unapply(x: X): Option[(A, B)]

}

abstract class DerivingCodecJson3[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec3(apply, unapply)(an, bn, cn)

  def apply(a: A, b: B, c: C): X

  def unapply(x: X): Option[(A, B, C)]

}

abstract class DerivingCodecJson4[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec4(apply, unapply)(an, bn, cn, dn)

  def apply(a: A, b: B, c: C, d: D): X

  def unapply(x: X): Option[(A, B, C, D)]

}

abstract class DerivingCodecJson5[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec5(apply, unapply)(an, bn, cn, dn, en)

  def apply(a: A, b: B, c: C, d: D, e: E): X

  def unapply(x: X): Option[(A, B, C, D, E)]

}

abstract class DerivingCodecJson6[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec6(apply, unapply)(an, bn, cn, dn, en, fn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F): X

  def unapply(x: X): Option[(A, B, C, D, E, F)]

}

abstract class DerivingCodecJson7[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec7(apply, unapply)(an, bn, cn, dn, en, fn, gn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G)]

}

abstract class DerivingCodecJson8[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec8(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H)]

}

abstract class DerivingCodecJson9[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec9(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I)]

}

abstract class DerivingCodecJson10[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec10(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J)]

}

abstract class DerivingCodecJson11[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec11(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K)]

}

abstract class DerivingCodecJson12[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec12(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L)]

}

abstract class DerivingCodecJson13[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec13(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)]

}

abstract class DerivingCodecJson14[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec14(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]

}

abstract class DerivingCodecJson15[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec15(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]

}

abstract class DerivingCodecJson16[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, P: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String, pn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec16(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on, pn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]

}

abstract class DerivingCodecJson17[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, P: EncodeJson: DecodeJson, Q: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String, pn: String, qn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec17(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on, pn, qn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]

}

abstract class DerivingCodecJson18[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, P: EncodeJson: DecodeJson, Q: EncodeJson: DecodeJson, R: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String, pn: String, qn: String, rn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec18(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on, pn, qn, rn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]

}

abstract class DerivingCodecJson19[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, P: EncodeJson: DecodeJson, Q: EncodeJson: DecodeJson, R: EncodeJson: DecodeJson, S: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String, pn: String, qn: String, rn: String, sn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec19(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on, pn, qn, rn, sn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]

}

abstract class DerivingCodecJson20[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, P: EncodeJson: DecodeJson, Q: EncodeJson: DecodeJson, R: EncodeJson: DecodeJson, S: EncodeJson: DecodeJson, T: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String, pn: String, qn: String, rn: String, sn: String, tn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec20(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on, pn, qn, rn, sn, tn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]

}

abstract class DerivingCodecJson21[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, P: EncodeJson: DecodeJson, Q: EncodeJson: DecodeJson, R: EncodeJson: DecodeJson, S: EncodeJson: DecodeJson, T: EncodeJson: DecodeJson, U: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String, pn: String, qn: String, rn: String, sn: String, tn: String, un: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec21(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on, pn, qn, rn, sn, tn, un)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]

}

abstract class DerivingCodecJson22[A: EncodeJson: DecodeJson, B: EncodeJson: DecodeJson, C: EncodeJson: DecodeJson, D: EncodeJson: DecodeJson, E: EncodeJson: DecodeJson, F: EncodeJson: DecodeJson, G: EncodeJson: DecodeJson, H: EncodeJson: DecodeJson, I: EncodeJson: DecodeJson, J: EncodeJson: DecodeJson, K: EncodeJson: DecodeJson, L: EncodeJson: DecodeJson, M: EncodeJson: DecodeJson, N: EncodeJson: DecodeJson, O: EncodeJson: DecodeJson, P: EncodeJson: DecodeJson, Q: EncodeJson: DecodeJson, R: EncodeJson: DecodeJson, S: EncodeJson: DecodeJson, T: EncodeJson: DecodeJson, U: EncodeJson: DecodeJson, V: EncodeJson: DecodeJson, X](an: String, bn: String, cn: String, dn: String, en: String, fn: String, gn: String, hn: String, in: String, jn: String, kn: String, ln: String, mn: String, nn: String, on: String, pn: String, qn: String, rn: String, sn: String, tn: String, un: String, vn: String) {
  implicit val codecInstance: CodecJson[X] =
    casecodec22(apply, unapply)(an, bn, cn, dn, en, fn, gn, hn, in, jn, kn, ln, mn, nn, on, pn, qn, rn, sn, tn, un, vn)

  def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V): X

  def unapply(x: X): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]

}

