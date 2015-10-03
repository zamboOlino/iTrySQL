/*
 * Copyright (c) 2015, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package de.kuehweg.sqltool.common.achievement;

/**
 * Lernfortschritt als Rangfolge.
 *
 * @author Michael Kühweg
 */
public enum NamedRank {

	TODDLER(0), PUPIL(25), STUDENT(50), MASTER(75), GENIOUS(100);

	private final int requiredPercentage;

	/**
	 * @param requiredPercentage
	 *            Punktzahl die zum Erreichen des Rangs erforderlich ist
	 */
	private NamedRank(final int requiredPercentage) {
		this.requiredPercentage = requiredPercentage;
	}

	/**
	 * @param rankingPoints
	 *            Ranking-System
	 * @return Den erreichten Rang im angegebenen Punktesystem für die aktuell
	 *         erreichten Achievements
	 */
	public static NamedRank achievedRankInRankingPoints(final RankingPoints rankingPoints) {
		final int maxPointsPossible = rankingPoints.maxPointsPossible();
		final int pointsAchieved = rankingPoints.pointsAchieved();
		final int percentageAchieved = maxPointsPossible == 0 ? 100 : pointsAchieved * 100 / maxPointsPossible;
		NamedRank maxRank = null;
		for (final NamedRank rank : values()) {
			if (rank.requiredPercentage <= percentageAchieved) {
				if (maxRank == null || maxRank.requiredPercentage <= rank.requiredPercentage) {
					maxRank = rank;
				}
			}
		}
		return maxRank;
	}
}
